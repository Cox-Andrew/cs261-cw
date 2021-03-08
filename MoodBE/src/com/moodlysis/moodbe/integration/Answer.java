package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;


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
	
	public Double getSentiment(String text) throws IOException, MalformedURLException {
		JSONParser parser = new JSONParser();
		Double com = 0.0;
		URL url = new URL("http://pythonapp.mood-net/v0/analyse?text=" + text);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setDoOutput(true);
		StringBuilder response = new StringBuilder();
		try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		}
		try {
			JSONObject mood = (JSONObject) parser.parse(response.toString());
			com = Double.valueOf(mood.get("compound").toString());
		} catch (ParseException e) {
			e.printStackTrace(this.writer);
		}
		
		Double value = com;
		return value;
	}


	@Override
	public int newAnswer(int attendeeID, int questionID, int eventFormID, Instant now, String response, boolean isAnonymous) throws MoodlysisInternalServerError {
	
		// TODO this function also need to signal to the sentiment analysis module that an answer has been submitted
		
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		ResultSet keys;
		
		try {
			
			conn.setAutoCommit(false);
			
			// TODO validate all this crap
//			"attendeeID": 3423423,
//			"eventID": 4242342,
//			"eventFormID": 34234324
//			"questionID": 4234324
			
			
			// insert a mood entry first so the insert into answers
			// doesn't violate not null constraint on the mood
			
			// first need to find the eventID for the mood table
			strStmt = ""
			+ "SELECT eventID FROM EventForms \n"
			+ "WHERE eventFormID = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventFormID);
			rs = stmt.executeQuery();
			rs.next();
			int eventID = rs.getInt("eventID");
			
			
			// create an entry in Mood for the sentiment analysis
			// want to do analysis before insertion here 
			// value = getSentiment(response);
			Double value = 0.0;
			try {
				value = getSentiment(response);
			} catch (MalformedURLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			} catch (IOException er) {
				er.printStackTrace();
				throw new MoodlysisInternalServerError(er.toString());
			}
			strStmt = ""
			+ "INSERT INTO Mood(moodid, eventID, value, timeSubmitted) \n"
			+ "VALUES (nextval('moodsmoodid'), ?, ?, ?);";
			stmt = conn.prepareStatement(strStmt, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, eventID);
			stmt.setDouble(2, value);
			stmt.setTimestamp(3, java.sql.Timestamp.from(now));
			stmt.execute();
			keys = stmt.getGeneratedKeys();
			keys.next();
			int moodID = keys.getInt(1);
			
			strStmt = ""
			+ "INSERT INTO Answers(AnswerID, QuestionID, AttendeeID, EventFormID, MoodID, IsEdited, TimeSubmitted, Response, IsAnonymous) \n"
			+ "VALUES (nextval('answersanswerid '),?,?,?,?,FALSE,?,?,?);";
			stmt = conn.prepareStatement(strStmt, Statement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, questionID);
			stmt.setInt(2, attendeeID);
			stmt.setInt(3, eventFormID);
			stmt.setInt(4, moodID);
			stmt.setTimestamp(5, Timestamp.from(now));
			stmt.setString(6, response);
			stmt.setBoolean(7, isAnonymous);
			stmt.executeUpdate();
			keys = stmt.getGeneratedKeys();
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
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
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
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}
		
	}
	

}
