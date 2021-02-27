package com.moodlysis.moodbe.requestexceptions;

public class MoodlysisForbidden extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MoodlysisForbidden() {
		super();
	}
	public MoodlysisForbidden(String message) {
		super(message);
	}
}
