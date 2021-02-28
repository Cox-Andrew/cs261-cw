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
import com.moodlysis.moodbe.integrationinterfaces.FeedbackInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public class Feedback implements FeedbackInterface {

	
	private Connection conn;
	private PrintWriter writer;
	
	public Feedback(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}

	
	private FeedbackInfo getFeedbackOfAnswerIDs(int[] answerIDs) {
		
		return null;
	}



	@Override
	public FeedbackInfo getAllFeedback(int eventID, int verificationIDHost) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FeedbackInfo getAllAttendeeFeedback(int eventID, int attendeeID, int verificationAttendeeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FeedbackInfo getFeedbackSince(int eventID, LocalDateTime since, int verificationIDHost) throws MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			// TODO verification
			
			strStmt = ""
			+ "SELECT answerid, questionID, attendeeid, eventformid, answers.moodid, isedited, answers.timesubmitted, response, formID, numInForm, mood.value \n"
			+ "FROM answers \n"
			+ "NATURAL JOIN Questions  \n"
			+ "JOIN Mood ON mood.moodID = answers.moodID \n"
			+ "WHERE answers.timeSubmitted > ? \n"
			+ "AND EXISTS ( \n"
			+ "	SELECT FROM EventForms \n"
			+ "	WHERE eventID = ? \n"
			+ "	AND formID = formID \n"
			+ ") \n"
			+ "ORDER BY formID, eventFormID, attendeeID, numInForm;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setTimestamp(1, java.sql.Timestamp.valueOf(since));
			stmt.setInt(2, eventID);
			rs = stmt.executeQuery();
			
			int currentEventFormID = -1;
			int currentAttendeeID = -1;
			
			FeedbackInfo feedbackInfo = new FeedbackInfo();
			feedbackInfo.eventID = eventID;
			feedbackInfo.timeUpdatedSince = since;
			feedbackInfo.list = new LinkedList<SubmissionInfo>();
			
			SubmissionInfo submissionInfo;
			
			
			if (!rs.next()) {
				// no results
				return null;
			}

			// json response is organised into submissions - create the first one
			submissionInfo = new SubmissionInfo();
			submissionInfo.formID = rs.getInt("formID");
			submissionInfo.eventFormID = rs.getInt("eventFormID");
			//TODO------
			submissionInfo.accountName = null;
			submissionInfo.attendeeID = rs.getInt("attendeeID");
			submissionInfo.timeUpdated = rs.getTimestamp("timeUpdated").toLocalDateTime();
			submissionInfo.isEdited = rs.getBoolean("isEdited");
			submissionInfo.answers = new LinkedList<Answer.AnswerInfo>();
			
			do {
				// different submission - add the previous and create a new one
				if (rs.getInt("eventFormID") != submissionInfo.eventFormID
				|| rs.getInt("attendeeID") != submissionInfo.attendeeID) {
					
					feedbackInfo.list.add(submissionInfo);
					
					submissionInfo = new SubmissionInfo();
					submissionInfo.formID = rs.getInt("formID");
					submissionInfo.eventFormID = rs.getInt("eventFormID");
					//TODO------
					submissionInfo.accountName = null;
					submissionInfo.attendeeID = rs.getInt("attendeeID");
					submissionInfo.timeUpdated = rs.getTimestamp("timeUpdated").toLocalDateTime();
					submissionInfo.isEdited = rs.getBoolean("isEdited");
					submissionInfo.answers = new LinkedList<Answer.AnswerInfo>();
				}
				
				Answer.AnswerInfo answerInfo = new Answer.AnswerInfo();
				answerInfo.questionID = rs.getInt("questionID");
				answerInfo.answerID = rs.getInt("answerID");
				answerInfo.moodValue = rs.getFloat("value");
				answerInfo.isEdited = rs.getBoolean("isEdited");
				answerInfo.timeSubmitted = rs.getTimestamp("timeSubmitted").toLocalDateTime();
				answerInfo.response = rs.getString("response");
				if (answerInfo.isEdited) 
					submissionInfo.isEdited = true;
				if (answerInfo.timeSubmitted.isAfter(submissionInfo.timeUpdated))
					submissionInfo.timeUpdated = answerInfo.timeSubmitted;
				submissionInfo.answers.add(answerInfo);
				
			} while (rs.next());
			
			// add the final
			feedbackInfo.list.add(submissionInfo);
			
			return feedbackInfo;
			
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			throw new MoodlysisInternalServerError();
		}			
		
	}

	@Override
	public FeedbackInfo getAttendeeFeedbackSince(int eventID, int attendeeID, LocalDateTime since,
			int verificationAttendeeID) {
		// TODO Auto-generated method stub
		return null;
	}}
