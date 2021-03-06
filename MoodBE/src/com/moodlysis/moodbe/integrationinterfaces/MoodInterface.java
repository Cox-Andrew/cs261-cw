package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public interface MoodInterface {
	
	
	public static class MoodInfo {
		public LocalDateTime timeSubmitted;
		public Float moodValue;
		public Integer answerID;
		public Integer attendeeID;
	}
	
	public static class MoodListInfo {
		public int eventID;
		public LocalDateTime timeSubmittedSince;
		public LinkedList<MoodInfo> list;
	}
	
	
	public String getAllMood(Connection conn, int eventID) throws SQLException;
	
	// GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}
	public MoodListInfo getMoodSince(int eventID, LocalDateTime since) throws MoodlysisInternalServerError;
	
	public String getAllAttendeeMood(Connection conn, int eventID, int attendeeID) throws SQLException;
	
	// GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}
	public String getAttendeeMoodSince(Connection conn, int eventID, int attendeeID, Date since) throws SQLException;
	
	// POST /v0/moods (explicit mood)
	public int newMood(int eventID, LocalDateTime timeSubmitted, double value) throws MoodlysisInternalServerError;

}
