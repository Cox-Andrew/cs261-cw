package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface FormInterface {

	// GET /v0/forms?hostID={hostID}
	// return a list of all formIDs a host has access to, including template forms - those with HostID=0 in the DB
	public int[] getFormIDsForHost(Connection conn, int hostID) throws SQLException;
	
	// GET /v0/forms/{formID}
	public String getJSON(Connection conn, int formID) throws SQLException;
	
	// POST /v0/forms
	public int newForm(Connection conn, int hostID, String formTitle, String formDescription) throws SQLException;
	
	// PUT /v0/forms/{formID}
	public boolean editForm(Connection conn, int formID, String formTitle, String formDescription) throws SQLException;
	
	// DELETE /v0/forms/{formID}
	public boolean deleteForm(Connection conn, int formID) throws SQLException;
	
	
}
