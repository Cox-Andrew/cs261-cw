package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.moodlysis.moodbe.integrationinterfaces.MoodInterface;

public class Mood implements MoodInterface {

	@Override
	public String getAllMood(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMoodSince(Connection conn, int eventID, Date since) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllAttendeeMood(Connection conn, int eventID, int attendeeID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttendeeMoodSince(Connection conn, int eventID, int attendeeID, Date since) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean newMood(Connection conn, int eventID, Date timeSubmitted, float value) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
