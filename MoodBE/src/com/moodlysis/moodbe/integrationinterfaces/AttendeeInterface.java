package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import com.moodlysis.moodbe.requestexceptions.*;

public interface AttendeeInterface {
		
	/* 		2 Cases.
	 * o User signs up. In that case all fields except expires (which has null) have values
	 * o Temp account - one created by the browser when the user is not signed in. All fields are null except expires.
	 */
	
	public static class AttendeeInfo {
		public int attendeeID;
		public String email;
		public String accountName;
		public LocalDateTime expires;
	}
	
	public AttendeeInfo getAttendeeInfo(int attendeeID, int verificationAttendeeID) throws MoodlysisForbidden, MoodlysisNotFound, MoodlysisInternalServerError;
	
	// POST /v0/attendees
	public int newAttendee(String name, String pass, String email, LocalDateTime expires) throws MoodlysisInternalServerError;
	
	public boolean editAttendee(int attendeeID, String name, String pass, String email, int verificationAttendeeID) throws MoodlysisForbidden, MoodlysisNotFound, MoodlysisInternalServerError;
	
	public boolean editAttendeeName(int attendeeID, String newName, int verificationAttendeeID) throws MoodlysisForbidden, MoodlysisNotFound, MoodlysisInternalServerError;
	
	public boolean deleteAttendee(int attendeeID, int verificationAttendeeID) throws MoodlysisForbidden, MoodlysisNotFound, MoodlysisInternalServerError;
}
