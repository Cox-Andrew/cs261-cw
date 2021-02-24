package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;

import org.json.simple.JSONObject;

import com.moodlysis.moodbe.integrationinterfaces.SeriesInterface;
import com.moodlysis.moodbe.DatabaseConnection;

public class Series implements SeriesInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Series(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}

	@Override
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

	@Override
	public int newSeries(String title, String desc, int hostID) {
		// TODO Auto-generated method stub
		//JDBC

		PreparedStatement seriesInsert  = null;
		ResultSet seriesKey = null;
		int seriesID = -1;
		try {
			conn.setAutoCommit(false);
    		String query = "INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'),?,?,?)";
    		seriesInsert = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		seriesInsert.setInt(1, hostID);
    		seriesInsert.setString(2, title);
    		seriesInsert.setString(3, desc);
			seriesInsert.executeUpdate();
			seriesKey = seriesInsert.getGeneratedKeys();
			seriesKey.next();
			seriesID = seriesKey.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			//TODO
			e.printStackTrace(this.writer);
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
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
				e.printStackTrace(this.writer);
			}
		}
		return seriesID;
	}

	@Override
	public boolean editSeries(int seriesID, String newTitle, String newDesc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSeries(int seriesID) {
		// TODO Auto-generated method stub
		return false;
	}

}
