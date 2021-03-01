package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.AttendeeInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisForbidden;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class Attendee implements AttendeeInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Attendee(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}

	
	
	@Override
	public int newAttendee(String accountName, String pass, String email, LocalDateTime expires) throws MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			conn.setAutoCommit(false);
			
			// otherwise insert the new values
			strStmt = ""
			+ "INSERT INTO Attendee (attendeeID, email, pass, accountName, expires) \n"
			+ "VALUES (nextval('attendeesattendeeid'), ?, ?, ?, ?);";
			stmt = conn.prepareStatement(strStmt, Statement.RETURN_GENERATED_KEYS);
			
			stmt.setString(1, email);
			stmt.setString(2, pass);
			stmt.setString(3, accountName);
			
			java.sql.Timestamp sqlTimestamp = null;
			if (expires != null) 
				sqlTimestamp = java.sql.Timestamp.valueOf(expires);
			
			stmt.setTimestamp(4, sqlTimestamp);

			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			int attendeeID = keys.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
			
			return attendeeID;
			
			
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
	public boolean editAttendee(Connection conn, int attendeeID, String name, String pass, String email, Date expires)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public AttendeeInfo getAttendeeInfo(int attendeeID, int verificationAttendeeID)
			throws MoodlysisForbidden, MoodlysisNotFound, MoodlysisInternalServerError {
		
		if (attendeeID != verificationAttendeeID) {
			throw new MoodlysisForbidden("You are not signed in as this user");
		}
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			strStmt = ""
			+ "SELECT email, accountName, expires FROM attendee \n"
			+ "WHERE attendeeID = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, attendeeID);
			rs= stmt.executeQuery();
			if (!rs.next()) {
				throw new MoodlysisNotFound("User not found. Account may have expired or been deleted.");
			}
			
			AttendeeInfo attendeeInfo = new AttendeeInfo();
			attendeeInfo.attendeeID = attendeeID;
			attendeeInfo.email = rs.getString("email");
			attendeeInfo.accountName = rs.getString("accountName");
			
			java.sql.Timestamp expiresSQL = rs.getTimestamp("expires");
			if (expiresSQL != null)
				attendeeInfo.expires = expiresSQL.toLocalDateTime();
			
			return attendeeInfo;
			
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
		
		
	}

}
