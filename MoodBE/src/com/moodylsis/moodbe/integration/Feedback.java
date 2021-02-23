package com.moodylsis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.moodylsis.moodbe.integrationinterfaces.FeedbackInterface;

public class Feedback implements FeedbackInterface {

	@Override
	public String getAllFeedback(Connection conn, int eventID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAllAttendeeFeedback(Connection conn, int eventID, int attendeeID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFeedbackSince(Connection conn, int eventID, Date since) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAttendeeFeedbackSince(Connection conn, int eventID, int attendeeID, Date since)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
