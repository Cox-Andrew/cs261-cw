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
	
	
	public static class seriesInfo {
		public int seriesID;
		public int hostID;
		public String title;
		public String description;
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
	
	public String getJSON(seriesInfo info) {
		JSONObject output = new JSONObject();
		output.put("seriesID", info.seriesID);
		output.put("hostID", info.hostID);
		output.put("title", info.title);
		output.put("description", info.description);
		return output.toJSONString();
	}
	
	
	
	public seriesInfo getSeries(int seriesID) {
		seriesInfo info = new seriesInfo();
		info.seriesID = seriesID;
		PreparedStatement seriesGet  = null;
		ResultSet table = null;
		int hostID = -1;
		String title = "";
		String desc = "";
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM SERIES WHERE SeriesID = ?";
    		seriesGet = conn.prepareStatement(query);
    		seriesGet.setInt(1, seriesID);
			table = seriesGet.executeQuery();
			table.next();
			hostID = table.getInt("hostID");
			title = table.getString("Title");
			desc = table.getString("Description");
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
				if (seriesGet != null) {
					seriesGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		info.hostID = hostID;
		info.title = title;
		info.description = desc;
		return info;
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
		// TODO fix so if error occurs return false but still execute finally statement
		PreparedStatement seriesEdit  = null;
		try {
			conn.setAutoCommit(false);
    		String query = "UPDATE SERIES SET Title = ?, Description = ? WHERE SeriesID = ?";
    		seriesEdit = conn.prepareStatement(query);
    		seriesEdit.setString(1, newTitle);
    		seriesEdit.setString(2, newDesc);
    		seriesEdit.setInt(3, seriesID);
    		seriesEdit.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			//TODO
			e.printStackTrace(this.writer);
			try {
				conn.rollback();
				return false;
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
				return false;
			}
		} finally {
			try {
				if (seriesEdit != null) {
					seriesEdit.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean deleteSeries(int seriesID) {
		// TODO Auto-generated method stub
		return false;
	}

}
