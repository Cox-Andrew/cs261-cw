package com.moodlysis.moodbe.integrationinterfaces;

public interface EventFormInterface {
	
	// POST /v0/event-forms
	public int newEventForm(int eventID, int formID, Boolean isActive);
	
	// PUT /v0/event-forms/{eventFormID}
	public boolean editEventForm(int eventFormID, int preceedingEventFormID ,Boolean isActive);
	
	// DELETE /v0/event-forms/{eventFormD}
	public boolean deleteEventForm(int eventFormID);

}
