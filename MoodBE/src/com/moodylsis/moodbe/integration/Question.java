package com.moodylsis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;

import com.moodylsis.moodbe.integrationinterfaces.QuestionInterface;

public class Question implements QuestionInterface {

	@Override
	public String getJSON(Connection conn, int questionID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int newQuestion(Connection conn, String questionType, int formID, int numInForm, String text, String options)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editQuestion(Connection conn, int questionID, String questionType, int numInForm, String text,
			String options) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuestion(Connection conn, int questionID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
