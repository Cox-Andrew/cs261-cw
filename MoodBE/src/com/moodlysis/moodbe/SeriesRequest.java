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
@WebServlet(urlPatterns = {"/v0/series", "/v0/series/*"})
public class SeriesRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SeriesRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String getJSON(int seriesID) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "seriesID": 1 }
		 */
		
		JSONObject output = new JSONObject();
		output.put("seriesID", seriesID);
		return output.toJSONString();
	}
	
	public String getJSON(Series.seriesInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		output.put("seriesID", info.seriesID);
		output.put("hostID", info.hostID);
		data.put("title", info.title);
		data.put("description", info.description);
		output.put("data", data);
		return output.toJSONString();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if /v0/series
		//response.getWriter().append("Served at: ").append(request.getContextPath() + "\n");
		//if  /v0/series/{seriesID}
		doGetSeries(request, response);
	}
	
	protected void doGetSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Series series = new Series(response.getWriter());
		int seriesID = GeneralRequest.getIDFromPath(request, response);
		
		Series.seriesInfo info = series.getSeries(seriesID);
		int hostID = info.hostID;
		String title = info.title;
		String description = info.description;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for description which isnt just empty string
		if (hostID == -1 || title.equals("") /*|| description.equals("")*/) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(info);
			response.getWriter().append(output + "\n");
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getRequestURI().equals("/v0/series")) {
			doNewSeries(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	protected void doNewSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Series series = new Series(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int hostID = -1;
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
			response.getWriter().append("\nhostID: " + hostID + "\ndata: " + data + "\n");
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
			String output = getJSON(seriesID);
			response.getWriter().append(output + "\n");
		}
		else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		//if /v0/series/{seriesID}
		doEditSeries(request, response);
	}
	
	protected void doEditSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Series series = new Series(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int seriesID = GeneralRequest.getIDFromPath(request, response);
		String data = "";
		String title = "";
		String description = "";
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"data": {
			 *			"title": "New series Title",
			 *			"description": "New description of Series."
			 *		}
			 *	}
			 *
			 * { "data": { "title": "New series Title", "description": "New description of Series." }}
			 * 
			 * JSON looks like the above
			 */
			data = putObject.get("data").toString();
			title = ((JSONObject) putObject.get("data")).get("title").toString();
			description = ((JSONObject) putObject.get("data")).get("description").toString();
			response.getWriter().append("\ndata: " + data + "\n");
			response.getWriter().append("title: " + title + "\n" + "description: " + description + "\n");
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//return;
		}
		if (series.editSeries(seriesID, title, description)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//assumes id not found
			//put into response body what was wrong (can be handled in jdbc)
		}
	}
	
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		//if /v0/series
		//response.getWriter().append("Served at: ").append(request.getContextPath() + "\n");
		//if  /v0/series/{seriesID}
		doDeleteSeries(request, response);
	}
	
	protected void doDeleteSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Series series = new Series(response.getWriter());
		int seriesID = GeneralRequest.getIDFromPath(request, response);
		
		if (series.deleteSeries(seriesID)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//assumes id not found
		}
	}

}
