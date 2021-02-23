package com.moodylsis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import com.moodylsis.moodbe.integrationinterfaces.AnswerInterface;

public class Answer implements AnswerInterface {

	@Override
	public boolean newAnswer(Connection conn, int questionID, int eventFormID, Date timeSubmitted, String response)
			throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean editAnswer(Connection conn, int answerID, Date timeSubmitted, String response) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
