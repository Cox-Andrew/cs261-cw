package com.moodlysis.moodbe.integrationinterfaces;

import java.time.Instant;
import java.time.LocalDateTime;

import com.moodlysis.moodbe.requestexceptions.*;

public interface AnswerInterface {
	
	public static class AnswerInfo {
		public int answerID;
		public int questionID;
		public int attendeeID;
		public int eventFormID;
		public int moodID;
		public Double moodValue;
		public boolean isEdited;
		public LocalDateTime timeSubmitted;
		public String response;
	}
	

	// POST /v0/answers
	// NOTE: when an answer is submitted, the database should also insert an entry into Mood
	public int newAnswer(int attendeeID, int questionID, int eventFormID, Instant timeSubmitted, String response, boolean isAnonymous) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// PUT /v0/answers/{answerID}
	public boolean editAnswer(int answerID, Instant now, boolean isAnonymous, String response) throws  MoodlysisInternalServerError, MoodlysisNotFound;

	public AnswerInfo getAnswerInfo(int answerID, int verificationID, boolean verificationIDIsHost) throws MoodlysisInternalServerError, MoodlysisForbidden, MoodlysisBadRequest;
}
