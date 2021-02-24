package com.moodlysis.moodbe;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.sql.*;

import com.moodlysis.moodbe.integration.Series;

/**
 * Servlet implementation class Series
 */
@WebServlet({"/v0/series", "/v0/series/*"})
public class SeriesRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SeriesRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getServletPath().equals("/v0/series")) {
			doNewSeries(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	protected void doNewSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Series series = new Series(response.getWriter());
		StringBuilder postData = new StringBuilder();
		BufferedReader reader = request.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				postData.append(line).append('\n');
			}
		} finally {
			reader.close();
		}
		//TODO test content-type header is application/json ( request.getContentType() )
		String jsonData = postData.toString();
		JSONParser postParser = new JSONParser();
		int hostID = 0;
		String data = "";
		String title = "";
		String description = "";
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"hostID" : 0,
			 *		"data": {
			 *			"title": "Series Title",
			 *			"description": "Description of Series."
			 *		}
			 *	}
			 *
			 * { "hostID" : 0, "data": { "title": "Series Title", "description": "Description of Series." }}
			 * 
			 * JSON looks like the above
			 */
			hostID = Integer.valueOf(postObject.get("hostID").toString());
			data = postObject.get("data").toString();
			title = ((JSONObject) postObject.get("data")).get("title").toString();
			description = ((JSONObject) postObject.get("data")).get("description").toString();
			response.getWriter().append("\nhostID: " + hostID + "\n" + "data: " + data + "\n");
			response.getWriter().append("title: " + title + "\n" + "description: " + description + "\n");
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
		}
		//CALL JDBC
		int seriesID = series.newSeries(title, description, hostID);
		
		// tell the caller that this is JSON content (move to front)
		if (seriesID != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = series.getJSON(seriesID);
			response.getWriter().append(output);
		}
		else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		doGet(request, response);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		doGet(request, response);
	}

}
