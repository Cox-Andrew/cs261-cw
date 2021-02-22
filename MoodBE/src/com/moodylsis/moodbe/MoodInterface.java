package com.moodylsis.moodbe;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface MoodInterface {
	
	public String getAllMood(Connection conn, int eventID) throws SQLException;
	
	// GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}
	public String getMoodSince(Connection conn, int eventID, Date since) throws SQLException;
	
	public String getAllAttendeeMood(Connection conn, int eventID, int attendeeID) throws SQLException;
	
	// GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}
	public String getAttendeeMoodSince(Connection conn, int eventID, int attendeeID, Date since) throws SQLException;
	
	// POST /v0/moods (explicit mood)
	public boolean newMood(Connection conn, int eventID, Date timeSubmitted, float value) throws SQLException;

}
