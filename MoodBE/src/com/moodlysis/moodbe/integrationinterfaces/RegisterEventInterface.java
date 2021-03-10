package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface RegisterEventInterface {
	
	public static class registerInfo {
		public int eventID;
		//public String cookie;
	}
	
	//GET /v0/invite-code?eventID={eventID}
	public String getInviteCode(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	//GET /v0/register-event?attendeeID={attendeeID}
	public int[] getEvents(int attendeeID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	//GET /v0/register-event?eventID={eventID}
	public int[] getAttendees(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	// POST /v0/register-event
	public registerInfo register(String inviteCode, int attendeeID) throws MoodlysisNotFound, MoodlysisInternalServerError;

}
