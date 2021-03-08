package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.PrintWriter;

import com.moodlysis.moodbe.integrationinterfaces.SeriesInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;
import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.GeneralRequest;

public class Series implements SeriesInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Series(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	@Override
	public seriesInfo getSeries(int seriesID) throws MoodlysisInternalServerError, MoodlysisNotFound {
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
			if (!table.next()) {
				throw new MoodlysisNotFound("Series not found. Series may have expired or been deleted.");
			}
			hostID = table.getInt("hostID");
			title = table.getString("Title");
			desc = table.getString("Description");
			if (table.wasNull()) {
				desc = "";
			}
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		info.hostID = hostID;
		info.title = title;
		info.description = desc;
		return info;
	}
	
	public int[] getSeriesIDsForHost(int hostID) throws MoodlysisInternalServerError {
		PreparedStatement seriesGet  = null;
		ResultSet table = null;
		int maxSeries = 100;
		int[] seriesIDs = new int[maxSeries];
		int i = 0;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM SERIES WHERE HostID = ?";
    		seriesGet = conn.prepareStatement(query);
    		seriesGet.setInt(1, hostID);
			table = seriesGet.executeQuery();
			while(table.next()) {
				seriesIDs[i] = table.getInt("SeriesID");
				i++;
				if (i == maxSeries) {
					maxSeries = maxSeries * 2;
					seriesIDs = GeneralRequest.extendArray(seriesIDs,maxSeries);
				}
			}
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		int[] returnIDs = new int[i];
		for (int j = 0; j < i; j++) {
			returnIDs[j] = seriesIDs[j];
		}
		return returnIDs;
	}

	@Override
	public int newSeries(String title, String desc, int hostID) throws MoodlysisInternalServerError {
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return seriesID;
	}

	@Override
	public boolean editSeries(int seriesID, String newTitle, String newDesc) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO fix so if error occurs return false but still execute finally statement
		PreparedStatement seriesEdit  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM SERIES WHERE SeriesID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, seriesID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Series not found. Series may have expired or been deleted.");
			}
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
			}
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (seriesEdit != null) {
					seriesEdit.close();
				}
				if (existCheck != null) {
					existCheck.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return true;
	}

	@Override
	public boolean deleteSeries(int seriesID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO Auto-generated method stub
		PreparedStatement seriesDelete  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM SERIES WHERE SeriesID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, seriesID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Series not found. Series may have expired or been deleted.");
			}
    		String query = "DELETE FROM SERIES WHERE SeriesID = ?";
    		seriesDelete = conn.prepareStatement(query);
    		seriesDelete.setInt(1, seriesID);
    		seriesDelete.executeUpdate();
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
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (seriesDelete != null) {
					seriesDelete.close();
				}
				if (existCheck != null) {
					existCheck.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return true;
	}

}
