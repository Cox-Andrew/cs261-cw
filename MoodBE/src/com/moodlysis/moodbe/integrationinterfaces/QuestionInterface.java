package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface QuestionInterface {
	
	// GET /v0/questions/{questionID}
	public String getJSON(Connection conn, int questionID) throws SQLException;
	
	// POST /v0/questions
	// NOTE: the numInForm of other questions may need to be considered when inserting
	public int newQuestion(Connection conn, String questionType, int formID, int numInForm, String text, String options) throws SQLException;
	
	// PUT /v0/questions/{questionID}
	// NOTE: again, the numInForm of other questions may need to be considered when editing
	public boolean editQuestion(Connection conn, int questionID, String questionType, int numInForm, String text, String options) throws SQLException;
	
	// DELETE /v0/questions/{questionID}
	public boolean deleteQuestion(Connection conn, int questionID) throws SQLException;
	

}
