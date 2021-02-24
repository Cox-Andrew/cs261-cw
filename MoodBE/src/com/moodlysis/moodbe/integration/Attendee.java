package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.moodlysis.moodbe.integrationinterfaces.AttendeeInterface;

public class Attendee implements AttendeeInterface {

	@Override
	public int newAttendee(Connection conn, String name, String pass, String email, Date expires) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editAttendee(Connection conn, int attendeeID, String name, String pass, String email, Date expires)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
