package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public interface EventInterface {
	
	// GET /v0/events/{eventID}
	public String getJSON(Connection conn, int eventID) throws SQLException;
	
	// GET /v0/events/{eventID}/invite-code
	public String getInviteCode(Connection conn, int eventID) throws SQLException;
	
	// GET /v0/event/{eventID}/analytics
	// don't know what this will return yet :(
	public String getAnalytics(Connection conn, int eventID) throws SQLException;
	
	// POST /v0/events
	/* Should return the ID of the new event, or null if not created */
	public int newEvent(Connection conn, int seriesID, String title, String description, Date start, Date end) throws SQLException;
	
	// POST /v0/events/{eventID}/forms
	public boolean newEventForm(Connection conn, int eventID, int formID, int preceedingFormID, Date timeStart, Date timeEnd) throws SQLException;
	
	// PUT /v0/events/{eventID}
	public boolean editEvent(Connection conn, int EventID, int seriesID, String newTitle, String newDescription, Date newStart, Date newEnd) throws SQLException;
	
	// DELETE /v0/events/{eventID}
	public boolean deleteEvent(Connection conn, int eventID) throws SQLException;
	
}
