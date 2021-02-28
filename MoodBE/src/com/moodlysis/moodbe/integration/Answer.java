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
import java.util.LinkedList;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.AnswerInterface;
import com.moodlysis.moodbe.integrationinterfaces.EventInterface.EventInfo;
import com.moodlysis.moodbe.requestexceptions.MoodlysisBadRequest;
import com.moodlysis.moodbe.requestexceptions.MoodlysisForbidden;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class Answer implements AnswerInterface {
	
	private Connection conn;
	private PrintWriter writer;

	public Answer(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}


	@Override
	public int newAnswer(int attendeeID, int questionID, int eventFormID, LocalDateTime timeSubmitted, String response, boolean isAnonymous) throws MoodlysisInternalServerError {
	
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			// TODO validate all this crap
//			"attendeeID": 3423423,
//			"eventID": 4242342,
//			"eventFormID": 34234324
//			"questionID": 4234324
			
			strStmt = ""
			+ "INSERT INTO Answers(AnswerID, QuestionID, AttendeeID, EventFormID, MoodID, IsEdited, TimeSubmitted, Response, IsAnonymous) \n"
			+ "VALUES (nextval('AnswerAnswerID'),?,?,?,NULL,FALSE,?,?,?);";
			stmt = conn.prepareStatement(strStmt, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, questionID);
			stmt.setInt(2, attendeeID);
			stmt.setInt(3, eventFormID);
			stmt.setTimestamp(4, Timestamp.valueOf(timeSubmitted));
			stmt.setString(5, response);
			stmt.setBoolean(6, isAnonymous);
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			keys.next();
			int answerID = keys.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
			return answerID;
			
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			e.printStackTrace(writer);
			throw new MoodlysisInternalServerError();
		}
		
	}

	@Override
	public boolean editAnswer(int answerID, Date timeSubmitted, String response) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public AnswerInfo getAnswerInfo(int answerID, int verificationID, boolean verificationIDIsHost)
			throws MoodlysisInternalServerError, MoodlysisForbidden, MoodlysisBadRequest {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
		
			// TODO verification. If verificationIDIsHost is false, then the verificationID provided is an attendeeID

			strStmt = ""
			+ "SELECT (answerid, questionid, attendeeid, eventformid, moodid, isedited, timesubmitted, response) \n"
			+ "FROM Answers \n"
			+ "WHERE answerid = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, answerID);
			rs = stmt.executeQuery();
			if (!rs.next()) throw new MoodlysisBadRequest("answerID not found");
			AnswerInfo answerInfo = new AnswerInfo();
			answerInfo.answerID = rs.getInt("answerInfo");
			answerInfo.questionID = rs.getInt("questionID");
			answerInfo.attendeeID = rs.getInt("attendeeID");
			answerInfo.eventFormID = rs.getInt("eventFormID");
			answerInfo.moodID = rs.getInt("moodID");
			answerInfo.isEdited = rs.getBoolean("isEdited");
			answerInfo.timeSubmitted = rs.getTimestamp("timeSubmitted").toLocalDateTime();
			answerInfo.response = rs.getString("response");
			return answerInfo;
			
			
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			e.printStackTrace(writer);
			throw new MoodlysisInternalServerError();
		}
		
	}
	

}
