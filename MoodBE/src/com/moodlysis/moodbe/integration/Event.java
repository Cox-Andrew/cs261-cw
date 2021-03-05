package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.Date;
//import java.util.Date; RAISES ERROR
import java.util.LinkedList;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.EventInterface;
import com.moodlysis.moodbe.requestexceptions.*;

public class Event implements EventInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	@Override
	public EventInfo getEventInfo(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			conn.setAutoCommit(false);
			strStmt = "SELECT * FROM Events WHERE EventID = ?";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			rs = stmt.executeQuery();
			
			if (!rs.next()) {
				throw new MoodlysisNotFound("eventID not found");
			} 
			
			EventInfo eventInfo = new EventInfo();
			eventInfo.eventID = eventID;
			eventInfo.seriesID = rs.getInt("seriesID");
			eventInfo.title = rs.getString("title");
			eventInfo.description = rs.getString("description");
			eventInfo.timeStart = rs.getTimestamp("timeStart").toLocalDateTime();
			eventInfo.timeEnd = rs.getTimestamp("timeEnd").toLocalDateTime();
			
			// find the formIDs and eventFormIDs in the event
			strStmt = ""
			+ "SELECT Forms.formID, EventForms.eventFormID  \n"
			+ "FROM Forms  \n"
			+ "JOIN EventForms ON EventForms.FormID = Forms.FormID \n"
			+ "WHERE eventID = ? \n"
			+ "ORDER BY EventForms.numInEvent;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			rs = stmt.executeQuery();
			
			LinkedList<Integer> formIDs = new LinkedList<Integer>();
			LinkedList<Integer> eventFormIDs = new LinkedList<Integer>();
			
			while (rs.next()) {
				formIDs.add(rs.getInt("formID"));
				eventFormIDs.add(rs.getInt("eventFormID"));
			}
			
			eventInfo.formIDs = formIDs.stream().mapToInt(i->i).toArray();
			eventInfo.eventFormIDs = eventFormIDs.stream().mapToInt(i->i).toArray();
			
			return eventInfo;

		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
	}
	
	public Event(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}


	@Override
	public String getInviteCode(int eventID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAnalytics(int eventID, int verificationHostID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int newEvent(int seriesID, String title, String description, LocalDateTime start, LocalDateTime end, int verificationHostID) throws MoodlysisInternalServerError, MoodlysisForbidden {
		
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
				
		try {
			conn.setAutoCommit(false);
			
			// check that the host has permission
			strStmt = ""
			+ "SELECT FROM Series \n"
			+ "WHERE seriesID = ? \n"
			+ "AND hostID = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, seriesID);
			stmt.setInt(2, verificationHostID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				// no results, therefore host does not have permission
				throw new MoodlysisForbidden("Not signed in as a host with ownership of this series");
			}
			
			// otherwise insert the new values
			strStmt = ""
			+ "INSERT INTO Events \n"
			+ "VALUES (nextval('SeriesSeriesID'),?,?,?,?,?);";   
			stmt = conn.prepareStatement(strStmt, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setInt(1, seriesID);
			stmt.setString(2, title);
			stmt.setString(3, description);
			stmt.setTimestamp(4, Timestamp.valueOf(start));
			stmt.setTimestamp(5, Timestamp.valueOf(end));

			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			int eventID = keys.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
			
			return eventID;
			
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}

		
	}

	@Override
	public boolean newEventForm(int eventID, int formID, int preceedingFormID, java.util.Date timeStart, java.util.Date timeEnd,
			int verificationHostID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editEvent(int eventID, String newTitle, String newDescription, LocalDateTime newStart,
			LocalDateTime newEnd, int verificationHostID) throws MoodlysisInternalServerError, MoodlysisForbidden {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
				
		try {
			conn.setAutoCommit(false);
			
			// check that the host has permission, AND that the event exists
			strStmt = ""
			+ "SELECT FROM Events \n"
			+ "WHERE Events.eventID = ? \n"
			+ "AND EXISTS ( \n"
			+ "	SELECT FROM Series \n"
			+ "	WHERE Series.seriesID = Events.seriesID \n"
			+ "	AND hostID = ? \n"
			+ ");";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			stmt.setInt(2, verificationHostID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				// no results, therefore host does not have permission
				throw new MoodlysisForbidden("You do not have access to this event, or the event does not exist");
			}
			
			strStmt = ""
			+ "UPDATE Events  \n"
			+ "SET title = ?, description = ?, timeStart = ?, timeEnd = ? \n"
			+ "WHERE eventID = ?";
			
			stmt.setString(1, newTitle);
			stmt.setString(2, newDescription);
			stmt.setTimestamp(3, Timestamp.valueOf(newStart));
			stmt.setTimestamp(4, Timestamp.valueOf(newEnd));
			stmt.setInt(5, eventID);

			stmt.executeUpdate();
			conn.commit();
			conn.setAutoCommit(true);
			
			return true;
			
			
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
	}

	@Override
	public boolean deleteEvent(int eventID, int verificationHostID) throws MoodlysisForbidden, MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			conn.setAutoCommit(false);
			
			// check that the host has permission, AND that the event exists
			strStmt = ""
			+ "SELECT FROM Events \n"
			+ "WHERE Events.eventID = ? \n"
			+ "AND EXISTS ( \n"
			+ "	SELECT FROM Series \n"
			+ "	WHERE Series.seriesID = Events.seriesID \n"
			+ "	AND hostID = ? \n"
			+ ");";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			stmt.setInt(2, verificationHostID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				// no results, therefore host does not have permission
				throw new MoodlysisForbidden("You do not have access to this event, or the event does not exist");
			}
			
			strStmt = ""
			+ "DELETE FROM Events \n"
			+ "WHERE EventID = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			stmt.execute();
			conn.commit();
			
			return true;
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
	}

	@Override
	public LinkedList<Integer> getSeriesEvents(int seriesID) throws MoodlysisBadRequest, MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			strStmt = ""
			+ "SELECT eventID FROM Events \n"
			+ "WHERE seriesID = ? \n"
			+ "ORDER BY timeStart;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, seriesID);
			rs = stmt.executeQuery();
			
			LinkedList<Integer> eventIDs = new LinkedList<Integer>();
			
			if (!rs.next()) {
				throw new MoodlysisBadRequest("eventID not found");
			} else do {
				eventIDs.add(rs.getInt(1));
			} while (rs.next());
			
			return eventIDs;
			
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
		
		
	}



}
