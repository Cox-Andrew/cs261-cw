package com.moodlysis.moodbe.requestexceptions;

public class MoodlysisNotFound extends Exception{

	private static final long serialVersionUID = 1L;
	
	public MoodlysisNotFound() {
		super();
	}
	public MoodlysisNotFound(String message) {
		super(message);
	}

}
