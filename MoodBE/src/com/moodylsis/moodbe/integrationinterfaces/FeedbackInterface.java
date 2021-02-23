package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface FeedbackInterface {
	
	// All the following return JSON
	
	// GET /v0/feedback?eventID={eventID}
	public String getAllFeedback(Connection conn, int eventID) throws SQLException;
	
	// GET /v0/feedback?eventID={eventID}&attendeeID={attendeeID}
	public String getAllAttendeeFeedback(Connection conn, int eventID, int attendeeID) throws SQLException;
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}
	public String getFeedbackSince(Connection conn, int eventID, Date since) throws SQLException;
	
	// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}
	public String getAttendeeFeedbackSince(Connection conn, int eventID, int attendeeID, Date since) throws SQLException;
	

}
