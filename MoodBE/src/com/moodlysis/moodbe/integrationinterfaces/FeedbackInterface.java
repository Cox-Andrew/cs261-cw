package com.moodlysis.moodbe.integrationinterfaces;

import java.time.LocalDateTime;
import java.util.LinkedList;

import com.moodlysis.moodbe.integrationinterfaces.AnswerInterface.AnswerInfo;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface FeedbackInterface {
	
	
	public static class FeedbackInfo {
		public int eventID;
		public LocalDateTime timeUpdatedSince;
		public LinkedList<SubmissionInfo> list;
	}
	public static class FeedbackAttendeeInfo {
		public int eventID;
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
	public FeedbackInfo getAllFeedback(int eventID, int verificationIDHost) throws MoodlysisInternalServerError;
	
	// GET /v0/feedback?eventID={eventID}&attendeeID={attendeeID}
	public FeedbackAttendeeInfo getAttendeeFeedback(int eventID, int attendeeID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}
	public FeedbackInfo getFeedbackSince(int eventID, LocalDateTime since, int verificationIDHost) throws MoodlysisInternalServerError;
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}
	public FeedbackInfo getAttendeeFeedbackSince(int eventID, int attendeeID, LocalDateTime since, int verificationAttendeeID);
	
	

}
