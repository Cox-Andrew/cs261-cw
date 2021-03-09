package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface RegisterEventInterface {
	
	public static class registerInfo {
		public int eventID;
		//public String cookie;
	}
	
	public String getInviteCode(int eventID) throws MoodlysisNotFound, MoodlysisInternalServerError;
	
	// POST /v0/register-event
	public registerInfo register(String inviteCode) throws MoodlysisNotFound, MoodlysisInternalServerError;

}
