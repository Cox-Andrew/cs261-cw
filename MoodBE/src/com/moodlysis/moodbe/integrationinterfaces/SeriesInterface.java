package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface SeriesInterface {
	
	//public interface seriesInfo {}
	
	//public String getJSON(seriesInfo info);
	
	
	public static class seriesInfo {
		public int seriesID;
		public int hostID;
		public String title;
		public String description;
	}
	
	// GET /v0/series/{seriesID}
	public seriesInfo getSeries(int seriesID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	
	
	// POST /v0/series
	// I didn't put events:list in because the series should be created before the events
	public int newSeries(String title, String desc, int hostID) throws MoodlysisInternalServerError;
	
	// PUT /v0/series/{seriesID}
	public boolean editSeries(int seriesID, String newTitle, String newDesc) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	// DELETE /v0/series/{seriesID}
	public boolean deleteSeries(int seriesID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	
	// there are other methods in the class diagram but I'm not sure if we need all of them, we can add them later if we do
	
}
