package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;

import com.moodlysis.moodbe.integrationinterfaces.AnswerInterface.AnswerInfo;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public interface FeedbackInterface {
	
	
	public static class FeedbackInfo {
		public int eventID;
		public LocalDateTime timeUpdatedSince;
		public LinkedList<SubmissionInfo> list;
	}
	
	public static class SubmissionInfo {
		public int formID;
		public int eventFormID;
		public String accountName;
		public int attendeeID;
		public LocalDateTime timeUpdated;
		public boolean isEdited;
		public LinkedList<AnswerInfo> answers;
	}
	
	// All the following return JSON
	
	// GET /v0/feedback?eventID={eventID}
	public FeedbackInfo getAllFeedback(int eventID, int verificationIDHost);
	
	// GET /v0/feedback?eventID={eventID}&attendeeID={attendeeID}
	public FeedbackInfo getAllAttendeeFeedback(int eventID, int attendeeID, int verificationAttendeeID);
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}
	public FeedbackInfo getFeedbackSince(int eventID, LocalDateTime since, int verificationIDHost) throws MoodlysisInternalServerError;
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}
	public FeedbackInfo getAttendeeFeedbackSince(int eventID, int attendeeID, LocalDateTime since, int verificationAttendeeID);
	
	

}
