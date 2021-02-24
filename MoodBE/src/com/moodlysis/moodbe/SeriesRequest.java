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
		
		//JDBC
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(response.getWriter());
		}
    	// JDBC connection to the database container
		String dburl = "jdbc:postgresql://database.mood-net:5432/mood?user=mooduser&password=password";
		/*
		 * example insert
		 * INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 1, 'SE Project', 'Software Engineering Project on Cybersecurity');
		 */
		PreparedStatement seriesInsert  = null;
		ResultSet seriesKey = null;
		int seriesID = 0;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dburl);
			conn.setAutoCommit(false);
    		String query = "INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'),?,?,?)";
    		seriesInsert = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		seriesInsert.setInt(1, hostID);
    		seriesInsert.setString(2, title);
    		seriesInsert.setString(3, description);
			seriesInsert.executeUpdate();
			seriesKey = seriesInsert.getGeneratedKeys();
			seriesKey.next();
			seriesID = seriesKey.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			//TODO
			e.printStackTrace(response.getWriter());
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(response.getWriter());
			}
		} finally {
			try {
				if (seriesInsert != null) {
						seriesInsert.close();
				}
				if (seriesKey != null) {
						seriesKey.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(response.getWriter());
			}
		}
		/*
		 * JSON Example Output
		 * { "seriesID": 1 }
		 */
		
		// tell the caller that this is JSON content (move to front)
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JSONObject output = new JSONObject();
		output.put("seriesID", seriesID);
		response.getWriter().append(output.toJSONString());
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub, do not use doGet
		doGet(request, response);
	}

}
