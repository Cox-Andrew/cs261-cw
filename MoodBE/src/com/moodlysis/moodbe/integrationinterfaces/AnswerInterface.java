package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import com.moodlysis.moodbe.requestexceptions.*;

public interface AnswerInterface {
	
	public static class AnswerInfo {
		public int answerID;
		public int questionID;
		public int attendeeID;
		public int eventFormID;
		public int moodID;
		public Float moodValue;
		public boolean isEdited;
		public LocalDateTime timeSubmitted;
		public String response;
	}
	

	// POST /v0/answers
	// NOTE: when an answer is submitted, the database should also insert an entry into Mood
	public int newAnswer(int attendeeID, int questionID, int eventFormID, Instant timeSubmitted, String response, boolean isAnonymous) throws MoodlysisInternalServerError;
	
	// PUT /v0/answers/{answerID}
	public boolean editAnswer(int answerID, Date timeSubmitted, String response) throws SQLException;

	public AnswerInfo getAnswerInfo(int answerID, int verificationID, boolean verificationIDIsHost) throws MoodlysisInternalServerError, MoodlysisForbidden, MoodlysisBadRequest;
}
