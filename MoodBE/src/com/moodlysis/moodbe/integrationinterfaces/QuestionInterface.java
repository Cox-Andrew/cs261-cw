package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface QuestionInterface {
	
	// GET /v0/questions/{questionID}
	//public String getQuestion(int questionID);
	
	// POST /v0/questions
	// NOTE: the numInForm of other questions may need to be considered when inserting
	public int newQuestion(int formID, String questionType, String text, String options);
	
	// PUT /v0/questions/{questionID}
	// NOTE: again, the numInForm of other questions may need to be considered when editing
	public boolean editQuestion(int questionID, String questionType, int numInForm, String text, String options);
	
	// DELETE /v0/questions/{questionID}
	public boolean deleteQuestion(int questionID);
	

}
