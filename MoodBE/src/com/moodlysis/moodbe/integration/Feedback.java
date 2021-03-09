package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
	public FeedbackInfo getAllFeedback(int eventID, int verificationIDHost) throws MoodlysisInternalServerError {
		
		String strStmt;
		PreparedStatement stmt;
		ResultSet rs;
		
		try {
			// find the oldest date in the database for this event
			strStmt = ""
			+ "SELECT timeSubmitted \n"
			+ "FROM Answers \n"
			+ "JOIN EventForms ON Answers.EventFormID = EventForms.EventFormID \n"
			+ "WHERE EventForms.EventID = ? \n"
			+ "ORDER BY Answers.timeSubmitted ASC \n"
			+ "LIMIT 1;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			rs = stmt.executeQuery();
			if (!rs.next()) {
				// no feedback for this event yet
				// create a blank output
				FeedbackInfo feedbackInfo = new FeedbackInfo();
				feedbackInfo.eventID = eventID;
				feedbackInfo.timeUpdatedSince = null;
				feedbackInfo.list = new LinkedList<SubmissionInfo>();
				return feedbackInfo;
			}
			LocalDateTime oldestEntry = rs.getTimestamp(1).toLocalDateTime();
			// round down second
			oldestEntry = oldestEntry.truncatedTo(ChronoUnit.SECONDS);
			return getFeedbackSince(eventID, oldestEntry, verificationIDHost);
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}	
		
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
			
			// find out what the eventFormId of the general feedback form is
			// needed to separate general feedback responses that may or may not be general feedback
			strStmt = ""
			+ "SELECT eventFormID \n"
			+ "FROM EventForms \n"
			+ "WHERE formID = 0 \n"
			+ "AND eventID = ?;";
			stmt = conn.prepareStatement(strStmt);
			stmt.setInt(1, eventID);
			rs = stmt.executeQuery();
			rs.next();
			int genFeedEventFormID = rs.getInt(1);
			
			
			strStmt = ""
			+ "SELECT answerid, questionID, attendee.attendeeid, eventformid, answers.moodid, isedited, answers.timesubmitted, response, formID, numInForm, mood.value, accountName, answers.IsAnonymous , questions.type \n"
			+ "FROM answers  \n"
			+ "NATURAL JOIN Questions   \n"
			+ "JOIN Mood ON mood.moodID = answers.moodID  \n"
			+ "JOIN Attendee ON attendee.attendeeID = answers.attendeeID  \n"
			+ "WHERE answers.timeSubmitted > ?  \n"
			+ "AND EXISTS (  \n"
			+ "	SELECT FROM EventForms  \n"
			+ "	WHERE eventID = ?  \n"
			+ "	AND EventForms.eventFormID = answers.eventFormID  \n"
			+ ")  \n"
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
				return feedbackInfo;
			}

			// json response is organised into submissions - create the first one
			submissionInfo = new SubmissionInfo();
			submissionInfo.formID = rs.getInt("formID");
			submissionInfo.eventFormID = rs.getInt("eventFormID");
			// accountname is filled in if an answer is encountered with isAnonymo
			submissionInfo.accountName = null;
			submissionInfo.attendeeID = rs.getInt("attendeeID");
			submissionInfo.timeUpdated = rs.getTimestamp("timesubmitted").toLocalDateTime();
			submissionInfo.isEdited = rs.getBoolean("isEdited");
			submissionInfo.answers = new LinkedList<Answer.AnswerInfo>();
			
			// so we don't create a new black submission on the case of a general feedback response
			// where rs.getInt("eventFormID") == genFeedEventFormID
			boolean firstItt = true;
			
			do {
				// different submission - add the previous and create a new one
				if ( !firstItt && (rs.getInt("eventFormID") != submissionInfo.eventFormID
				|| rs.getInt("attendeeID") != submissionInfo.attendeeID || rs.getInt("eventFormID") == genFeedEventFormID)) {
					
					feedbackInfo.list.add(submissionInfo);
					
					submissionInfo = new SubmissionInfo();
					submissionInfo.formID = rs.getInt("formID");
					submissionInfo.eventFormID = rs.getInt("eventFormID");
					//TODO------
					submissionInfo.accountName = null;
					submissionInfo.attendeeID = rs.getInt("attendeeID");
					submissionInfo.timeUpdated = rs.getTimestamp("timesubmitted").toLocalDateTime();
					submissionInfo.isEdited = rs.getBoolean("isEdited");
					submissionInfo.answers = new LinkedList<Answer.AnswerInfo>();
				}
				
				firstItt = false;
				
				Answer.AnswerInfo answerInfo = new Answer.AnswerInfo();
				answerInfo.questionID = rs.getInt("questionID");
				answerInfo.answerID = rs.getInt("answerID");
				answerInfo.moodValue = rs.getFloat("value");
				if (rs.wasNull())
					answerInfo.moodValue = null;
				if (rs.getString("type").equals("multi") || rs.getString("type").equals("rating"))
					answerInfo.moodValue = null;
				answerInfo.isEdited = rs.getBoolean("isEdited");
				answerInfo.timeSubmitted = rs.getTimestamp("timeSubmitted").toLocalDateTime();
				answerInfo.response = rs.getString("response");
				if (answerInfo.isEdited) 
					submissionInfo.isEdited = true;
				if (answerInfo.timeSubmitted.isAfter(submissionInfo.timeUpdated))
					submissionInfo.timeUpdated = answerInfo.timeSubmitted;
				submissionInfo.answers.add(answerInfo);
				if (!rs.getBoolean("isAnonymous"))
					submissionInfo.accountName = rs.getString("accountname");
				
			} while (rs.next());
			
			// add the final
			feedbackInfo.list.add(submissionInfo);
			
			return feedbackInfo;
			
			
		} catch (SQLException e) {
			e.printStackTrace(writer);
			e.printStackTrace();
			throw new MoodlysisInternalServerError(e.toString());
		}			
		
	}

	@Override
	public FeedbackInfo getAttendeeFeedbackSince(int eventID, int attendeeID, LocalDateTime since,
			int verificationAttendeeID) {
		// TODO Auto-generated method stub
		return null;
	}}
