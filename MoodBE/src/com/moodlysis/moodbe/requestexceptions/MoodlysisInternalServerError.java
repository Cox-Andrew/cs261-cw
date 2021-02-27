package com.moodlysis.moodbe.requestexceptions;

public class MoodlysisInternalServerError extends Exception{

	private static final long serialVersionUID = 1L;
	
	public MoodlysisInternalServerError() {
		super();
	}
	public MoodlysisInternalServerError(String message) {
		super(message);
	}

}
