package com.moodlysis.moodbe.integrationinterfaces;

public interface SeriesInterface {
	
	//public interface seriesInfo {}
	
	//public String getJSON(seriesInfo info);
	
	
	// GET /v0/series/{seriesID}
	//public seriesInfo getSeries(int seriesID);
	
	
	
	// POST /v0/series
	// I didn't put events:list in because the series should be created before the events
	public int newSeries(String title, String desc, int hostID);
	
	// PUT /v0/series/{seriesID}
	public boolean editSeries(int seriesID, String newTitle, String newDesc);
	
	// DELETE /v0/series/{seriesID}
	public boolean deleteSeries(int seriesID);
	
	
	// there are other methods in the class diagram but I'm not sure if we need all of them, we can add them later if we do
	
}