package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Series;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;


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
    
    @SuppressWarnings("unchecked")
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
	
    @SuppressWarnings("unchecked")
	public String getJSON(Series.seriesInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		output.put("seriesID", info.seriesID);
		output.put("hostID", info.hostID);
		data.put("title", info.title);
		data.put("description", info.description);
		output.put("eventIDs", info.eventIDs);
		output.put("data", data);
		return output.toJSONString();
	}
    
    @SuppressWarnings("unchecked")
    public String getJSON(int[] seriesIDs) {
    	JSONObject output = new JSONObject();
    	JSONArray forms = new JSONArray();
    	if (seriesIDs != null) {
	    	for (int i = 0; i < seriesIDs.length; i++) {
	    		forms.add(seriesIDs[i]);
	    	}
    	}
    	output.put("seriesIDs", forms);
    	return output.toJSONString();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/series/([1-9])([0-9]*)")) {
			doGetSeries(request, response);
		}
		else if (request.getRequestURI().equals("/v0/series")) {
			doGetHostSeries(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doGetSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int seriesID = GeneralRequest.getIDFromPath(request, response);
		
		Connection conn = DatabaseConnection.getConnection();
		Series series = new Series(response.getWriter(), conn);

	
		Series.seriesInfo info;
		try {
			info = series.getSeries(seriesID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int hostID = info.hostID;
		String title = info.title;
		//String description = info.description;
		
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
	
	protected void doGetHostSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int hostID = GeneralRequest.getIDFromQuery(request, response, "hostID");
		Connection conn = DatabaseConnection.getConnection();
		Series series = new Series(response.getWriter(), conn);

		int[] seriesIDs;
		try {
			seriesIDs = series.getSeriesIDsForHost(hostID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// tell the caller that this is JSON content (move to front)
		if (seriesIDs == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		else if (seriesIDs.length == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(seriesIDs);
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
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int hostID = -1;
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
			title = ((JSONObject) postObject.get("data")).get("title").toString();
			description = ((JSONObject) postObject.get("data")).get("description").toString();
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		//CALL JDBC		
		Connection conn = DatabaseConnection.getConnection();
		Series series = new Series(response.getWriter(), conn);

		int seriesID;
		try {
			seriesID = series.newSeries(title, description, hostID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
		if (request.getRequestURI().matches("/v0/series/([1-9])([0-9]*)")) {
			doEditSeries(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doEditSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int seriesID = GeneralRequest.getIDFromPath(request, response);
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
			title = ((JSONObject) putObject.get("data")).get("title").toString();
			description = ((JSONObject) putObject.get("data")).get("description").toString();
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}		
		
		Connection conn = DatabaseConnection.getConnection();
		Series series = new Series(response.getWriter(), conn);

		try {
			series.editSeries(seriesID, title, description);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		if (request.getRequestURI().matches("/v0/series/([1-9])([0-9]*)")) {
			doDeleteSeries(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doDeleteSeries(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int seriesID = GeneralRequest.getIDFromPath(request, response);
		
		Connection conn = DatabaseConnection.getConnection();
		Series series = new Series(response.getWriter(), conn);

		try {
			series.deleteSeries(seriesID);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
