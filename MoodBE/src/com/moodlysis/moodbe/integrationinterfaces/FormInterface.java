package com.moodlysis.moodbe.integrationinterfaces;

public interface FormInterface {

	// GET /v0/forms?hostID={hostID}
	// return a list of all formIDs a host has access to, including template forms - those with HostID=0 in the DB
	public int[] getFormIDsForHost(int hostID);
	
	// GET /v0/forms/{formID}
	//public String getForm(int formID);
	
	// POST /v0/forms
	public int newForm(int hostID, String formTitle, String formDescription);
	
	// PUT /v0/forms/{formID}
	public boolean editForm(int formID, String formTitle, String formDescription);
	
	// DELETE /v0/forms/{formID}
	public boolean deleteForm(int formID);
	
	
}
