package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.GeneralRequest;
import com.moodlysis.moodbe.integrationinterfaces.RegisterEventInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class RegisterEvent implements RegisterEventInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public RegisterEvent(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	@Override
	public int[] getAttendees(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError{
		PreparedStatement attendeesGet  = null;
		ResultSet table = null;
		int maxAttendees = 100;
		int[] attendeeIDs = new int[maxAttendees];
		int i = 0;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM REGISTEREVENTS WHERE EventID = ?";
    		attendeesGet = conn.prepareStatement(query);
    		attendeesGet.setInt(1, eventID);
			table = attendeesGet.executeQuery();
			while(table.next()) {
				attendeeIDs[i] = table.getInt("AttendeeID");
				i++;
				if (i == maxAttendees) {
					maxAttendees = maxAttendees * 2;
					attendeeIDs = GeneralRequest.extendArray(attendeeIDs,maxAttendees);
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
				if (attendeesGet != null) {
					attendeesGet.close();
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
			returnIDs[j] = attendeeIDs[j];
		}
		return returnIDs;
	}
	
	@Override
	public String getInviteCode(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError{
		String inviteCode = null;
		PreparedStatement inviteCodeGet  = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM EVENTS WHERE eventID = ?";
    		inviteCodeGet = conn.prepareStatement(query);
    		inviteCodeGet.setInt(1, eventID);
			table = inviteCodeGet.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Event not found. Event may have expired or been deleted.");
			}
			inviteCode = table.getString("inviteCode");
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
				if (inviteCodeGet != null) {
					inviteCodeGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return inviteCode;
	}
	
	@Override
	public registerInfo register(String inviteCode, int attendeeID) throws MoodlysisNotFound, MoodlysisInternalServerError {
		registerInfo info = new registerInfo();
		PreparedStatement registerGet  = null;
		ResultSet table = null;
		PreparedStatement registerInsert = null;
		PreparedStatement existCheck = null;
		ResultSet table2 = null;
		int eventID = -1;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM EVENTS WHERE inviteCode = ?";
    		registerGet = conn.prepareStatement(query);
    		registerGet.setString(1, inviteCode);
			table = registerGet.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Event not found. Event may have expired or been deleted.");
			}
			eventID = table.getInt("eventID");
			String queryAttendee = "SELECT * FROM ATTENDEES WHERE AttendeeID = ?";
			existCheck = conn.prepareStatement(queryAttendee);
			existCheck.setInt(1, attendeeID);
			table2 = existCheck.executeQuery();
			if (!table2.next()) {
				throw new MoodlysisNotFound("Attendee not found. Attendee may have expired or been deleted.");
			}
			String queryInsert = "INSERT INTO REGISTEREVENTS VALUES (?,?)";
			registerInsert = conn.prepareStatement(queryInsert);
			registerInsert.setInt(1, attendeeID);
			registerInsert.setInt(2, eventID);
			registerInsert.executeUpdate();
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
				if (registerGet != null) {
					registerGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		info.eventID = eventID;
		//info.cookie = cookie;
		return info;
	}
	
	

}
