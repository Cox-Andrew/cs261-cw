package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

public interface QuestionInterface {
	
	public static class questionInfo {
		public int questionID;
		public int formID;
		public String type;
		public String text;
		public String options;
		public int numInForm;
	}
	
	// GET /v0/questions/{questionID}
	public questionInfo getQuestion(int questionID) throws MoodlysisInternalServerError;
	
	// POST /v0/questions
	// NOTE: the numInForm of other questions may need to be considered when inserting
	public int newQuestion(int formID, String questionType, String text, String options) throws MoodlysisInternalServerError;
	
	// PUT /v0/questions/{questionID}
	// NOTE: again, the numInForm of other questions may need to be considered when editing
	public boolean editQuestionDetails(int questionID, String questionType, String text, String options) throws MoodlysisInternalServerError;
	
	// DELETE /v0/questions/{questionID}
	public boolean deleteQuestion(int questionID) throws MoodlysisInternalServerError;
	

}
