package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.moodlysis.moodbe.DatabaseConnection;
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
	public registerInfo register(String inviteCode) throws MoodlysisNotFound, MoodlysisInternalServerError {
		registerInfo info = new registerInfo();
		PreparedStatement registerGet  = null;
		ResultSet table = null;
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
