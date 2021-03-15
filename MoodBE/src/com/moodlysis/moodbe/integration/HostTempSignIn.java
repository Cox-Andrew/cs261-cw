package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class HostTempSignIn {
	
	private Connection conn;
	private PrintWriter writer;
	
	public HostTempSignIn(PrintWriter writer, Connection conn) {
		this.conn = conn;
		this.writer = writer;
	}

	
	public int getHostIDFromEmailAndPassword(String email, String password) throws MoodlysisNotFound, MoodlysisInternalServerError {
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			strStmt = ""
			+ "SELECT hostID FROM Host \n"
			+ "WHERE email = ? AND pass = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setString(1, email);
			stmt.setString(2, password);
			rs = stmt.executeQuery();
			if (!rs.next()) 
				throw new MoodlysisNotFound("Invalid credentials");
			return rs.getInt("hostID");
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
		
	}
	
}
