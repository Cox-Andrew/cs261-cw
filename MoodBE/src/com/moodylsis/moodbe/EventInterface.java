package com.moodylsis.moodbe;

import java.sql.Connection;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public interface EventInterface {
	
	// GET /v0/events/{eventID}
	public String get(Connection conn, int eventID);
	
	// GET /v0/events/{eventID}/invite-code
	public String getInviteCode(Connection conn, int eventID);
	
	// GET /v0/event/{eventID}/analytics
	public String getAnalytics(Connection conn, int eventID);
	
	// POST /v0/events
	/* Should return the ID of the new event, or null if not created */
	public String newEvent(Connection conn, int seriesID, String title, String description, Date start, Date end);
	
	// POST /v0/events/{eventID}/forms
	public boolean newEventForm(Connection conn, int eventID, int formID, int preceedingFormID, Date timeStart, Date timeEnd);
	
	// PUT /v0/events/{eventID}
	public boolean editEvent(Connection conn, int EventID, int seriesID, String newTitle, String newDescription, Date newStart, Date newEnd);
	
	// DELETE /v0/events/{eventID}
	public boolean deleteEvent(Connection conn, int eventID);
	
}
