package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletResponse;

import com.moodlysis.moodbe.requestexceptions.*;

public interface EventInterface {
	
	public static class EventInfo {
		public int eventID;
		public int seriesID;
		public int[] formIDs;
		public int[] eventFormIDs;
		public String title;
		public String description;
		public Date timeStart;
		public Date timeEnd;
	}
	
	// GET /v0/events/{eventID}
	public EventInfo getEventInfo(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	// GET /v0/events/{eventID}/invite-code
	public String getInviteCode(int eventID);
	
	// GET /v0/event/{eventID}/analytics
	// don't know what this will return yet :(
	public String getAnalytics(int eventID, int verificationHostID);
	
	// POST /v0/events
	/* Should return the ID of the new event, or null if not created */
	public int newEvent(int seriesID, String title, String description, LocalDateTime start, LocalDateTime end, int verificationHostID) throws MoodlysisInternalServerError, MoodlysisForbidden;
	
	// POST /v0/events/{eventID}/forms
	public boolean newEventForm(int eventID, int formID, int preceedingFormID, Date timeStart, Date timeEnd, int verificationHostID);
	
	// PUT /v0/events/{eventID}
	public boolean editEvent(int EventID, String newTitle, String newDescription, LocalDateTime newStart, LocalDateTime newEnd, int verificationHostID) throws MoodlysisInternalServerError, MoodlysisForbidden;
	
	// DELETE /v0/events/{eventID}
	public boolean deleteEvent(int eventID, int verificationHostID) throws MoodlysisForbidden, MoodlysisInternalServerError;
	
	public LinkedList<Integer> getSeriesEvents(int seriesID) throws MoodlysisBadRequest, MoodlysisInternalServerError;
	
}
