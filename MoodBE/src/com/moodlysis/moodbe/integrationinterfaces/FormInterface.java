package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface FormInterface {
	
	public static class formInfo {
		public int formID;
		public int hostID;
		public String title;
		public String description;
		public int[] questionIDs;
	}

	// GET /v0/forms?hostID={hostID}
	// return a list of all formIDs a host has access to, including template forms - those with HostID=0 in the DB
	public int[] getFormIDsForHost(int hostID) throws MoodlysisInternalServerError;
	
	// GET /v0/forms/{formID}
	public formInfo getForm(int formID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// POST /v0/forms
	public int newForm(int hostID, String formTitle, String formDescription) throws MoodlysisInternalServerError;
	
	// PUT /v0/forms/{formID}
	public boolean editForm(int formID, String formTitle, String formDescription) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// DELETE /v0/forms/{formID}
	public boolean deleteForm(int formID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	
}
