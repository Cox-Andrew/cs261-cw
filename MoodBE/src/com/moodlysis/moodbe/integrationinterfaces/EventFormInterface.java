package com.moodlysis.moodbe.integrationinterfaces;

import java.time.LocalDateTime;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface EventFormInterface {
	
	public static class eventFormInfo {
		public int eventFormID;
		public int eventID;
		public int formID;
		public int numInEvent;
		public Boolean isActive;
		public LocalDateTime timeStart;
		public LocalDateTime timeEnd;
	}
	
	
	
	// GET /v0/event-forms/{eventFormID}
	public eventFormInfo getEventForm(int eventFormID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// POST /v0/event-forms
	public int newEventForm(int eventID, int formID, int previousID, Boolean isActive, LocalDateTime timeStart, LocalDateTime timeEnd) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// PUT /v0/event-forms/{eventFormID}
	public boolean editEventForm(int eventFormID, int preceedingEventFormID ,Boolean isActive) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// DELETE /v0/event-forms/{eventFormD}
	public boolean deleteEventForm(int eventFormID) throws MoodlysisInternalServerError, MoodlysisNotFound;

}
