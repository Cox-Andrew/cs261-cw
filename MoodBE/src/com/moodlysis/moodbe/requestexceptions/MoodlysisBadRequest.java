package com.moodlysis.moodbe.requestexceptions;

public class MoodlysisBadRequest extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MoodlysisBadRequest() {
		super();
	}
	public MoodlysisBadRequest(String message) {
		super(message);
	}
}
