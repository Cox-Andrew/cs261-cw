package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.MoodInterface;
import com.moodlysis.moodbe.integrationinterfaces.FeedbackInterface.FeedbackInfo;
import com.moodlysis.moodbe.integrationinterfaces.FeedbackInterface.SubmissionInfo;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public class Mood implements MoodInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Mood(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}

	@Override
	public String getAllMood(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MoodListInfo getMoodSince(int eventID, LocalDateTime since) throws MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			// TODO verification
			
			strStmt = ""
			+ "SELECT mood.moodID, value, mood.timeSubmitted, Answers.answerid \n"
			+ "FROM Mood \n"
			+ "LEFT JOIN Answers on Answers.moodID = Mood.moodID \n"
			+ "WHERE eventID = ? \n"
			+ "AND mood.timeSubmitted > ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			stmt.setTimestamp(2, java.sql.Timestamp.valueOf(since));
			rs = stmt.executeQuery();
			
			MoodListInfo moodListInfo = new MoodListInfo();
			moodListInfo.list = new LinkedList<Mood.MoodInfo>();
			moodListInfo.eventID = eventID;
			moodListInfo.timeSubmittedSince = since;
			
			
			while (rs.next()) {
				Mood.MoodInfo moodInfo = new Mood.MoodInfo();
				moodInfo.timeSubmitted = rs.getTimestamp("timeSubmitted").toLocalDateTime();
				moodInfo.moodValue = rs.getFloat("value");
				if (rs.wasNull())
					moodInfo.moodValue = null;
				moodInfo.answerID = rs.getInt("answerID");
				if (rs.wasNull()) 
					moodInfo.answerID = null;
				moodInfo.attendeeID = null; // TODO
				
				moodListInfo.list.add(moodInfo);
			}
			
			return moodListInfo;
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			throw new MoodlysisInternalServerError();
		}	
		
	}

	@Override
	public String getAllAttendeeMood(Connection conn, int eventID, int attendeeID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttendeeMoodSince(Connection conn, int eventID, int attendeeID, Date since) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean newMood(Connection conn, int eventID, Date timeSubmitted, float value) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
