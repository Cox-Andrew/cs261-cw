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

public class AttendeeTempSignIn {
	
	private Connection conn;
	private PrintWriter writer;
	
	public AttendeeTempSignIn(PrintWriter writer, Connection conn) {
		this.conn = conn;
		this.writer = writer;
	}

	
	public int getAttendeeIDFromEmailAndPassword(String email, String password) throws MoodlysisNotFound, MoodlysisInternalServerError {
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			strStmt = ""
			+ "SELECT attendeeID FROM Attendee \n"
			+ "WHERE email = ? AND pass = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (!rs.next()) 
				throw new MoodlysisNotFound("account not found");
			return rs.getInt("attendeeID");
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
		
	}
	
}
