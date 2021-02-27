package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public interface AnswerInterface {
	

	// POST /v0/answers
	// NOTE: when an answer is submitted, the database should also insert an entry into Mood
	public int newAnswer(int attendeeID, int questionID, int eventFormID, LocalDateTime timeSubmitted, String response, boolean isAnonymous) throws MoodlysisInternalServerError;
	
	// PUT /v0/answers/{answerID}
	public boolean editAnswer(int answerID, Date timeSubmitted, String response) throws SQLException;

}
