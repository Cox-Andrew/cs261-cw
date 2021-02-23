package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface AttendeeInterface {
		
	/* 		2 Cases.
	 * o User signs up. In that case all fields except expires (which has null) have values
	 * o Temp account - one created by the browser when the user is not signed in. All fields are null except expires.
	 */
	
	// POST /v0/attendees
	public int newAttendee(Connection conn, String name, String pass, String email, Date expires) throws SQLException;
	
	public boolean editAttendee(Connection conn, int attendeeID, String name, String pass, String email, Date expires) throws SQLException;
	

}
