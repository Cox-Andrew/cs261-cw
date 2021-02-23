package com.moodylsis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;

import com.moodylsis.moodbe.integrationinterfaces.FormInterface;

public class Form implements FormInterface {

	@Override
	public int[] getFormIDsForHost(Connection conn, int hostID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJSON(Connection conn, int formID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int newForm(Connection conn, int hostID, String formTitle, String formDescription) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editForm(Connection conn, int formID, String formTitle, String formDescription) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteForm(Connection conn, int formID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
