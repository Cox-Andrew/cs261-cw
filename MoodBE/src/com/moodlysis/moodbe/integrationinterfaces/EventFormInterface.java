package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public interface EventFormInterface {
	
	public static class eventFormInfo {
		public int eventFormID;
		public int eventID;
		public int formID;
		public int numInEvent;
		public Boolean isActive;
	}
	
	
	
	// GET /v0/event-forms/{eventFormID}
	public eventFormInfo getEventForm(int eventFormID) throws MoodlysisInternalServerError;
	
	// POST /v0/event-forms
	public int newEventForm(int eventID, int formID, Boolean isActive) throws MoodlysisInternalServerError;
	
	// PUT /v0/event-forms/{eventFormID}
	public boolean editEventForm(int eventFormID, int preceedingEventFormID ,Boolean isActive) throws MoodlysisInternalServerError;
	
	// DELETE /v0/event-forms/{eventFormD}
	public boolean deleteEventForm(int eventFormID) throws MoodlysisInternalServerError;

}
