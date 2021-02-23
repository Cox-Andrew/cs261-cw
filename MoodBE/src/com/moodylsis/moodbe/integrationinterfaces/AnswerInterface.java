package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public interface AnswerInterface {
	

	// POST /v0/answers
	// NOTE: when an answer is submitted, the database should also insert an entry into Mood
	public boolean newAnswer(Connection conn, int questionID, int eventFormID, Date timeSubmitted, String response) throws SQLException;
	
	// PUT /v0/answers/{answerID}
	public boolean editAnswer(Connection conn, int answerID, Date timeSubmitted, String response) throws SQLException;

}
