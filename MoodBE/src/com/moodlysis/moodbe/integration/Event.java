package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.moodlysis.moodbe.integrationinterfaces.EventInterface;

public class Event implements EventInterface {

	@Override
	public String getJSON(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getInviteCode(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAnalytics(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int newEvent(Connection conn, int seriesID, String title, String description, Date start, Date end)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean newEventForm(Connection conn, int eventID, int formID, int preceedingFormID, Date timeStart,
			Date timeEnd) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editEvent(Connection conn, int EventID, int seriesID, String newTitle, String newDescription,
			Date newStart, Date newEnd) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteEvent(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
