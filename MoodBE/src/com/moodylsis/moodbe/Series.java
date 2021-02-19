package com.moodylsis.moodbe;

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

/**
 * Servlet implementation class Series
 */
@WebServlet("/v0/series")
public class Series extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Series() {
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
		//TODO test content-type header is application/json
		String jsonData = postData.toString();
		JSONParser postParser = new JSONParser();
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"hostID" : 2342341,
			 *		"data": {
			 *			"title": "Series Title",
			 *			"description": "Description of Series."
			 *		}
			 *	}
			 *
			 * { "hostID" : 2342341, "data": { "title": "Series Title", "description": "Description of Series." }}
			 * 
			 * JSON looks like the above
			 */
			int hostID = Integer.valueOf(postObject.get("hostID").toString());
			String data = postObject.get("data").toString();
			String title = ((JSONObject) postObject.get("data")).get("title").toString();
			String description = ((JSONObject) postObject.get("data")).get("description").toString();
			response.getWriter().append("\nhostID: " + hostID + "\n" + "data: " + data + "\n");
			response.getWriter().append("title: " + title + "\n" + "description: " + description + "\n");
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
		}
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		doGet(request, response);
	}

}
