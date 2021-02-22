package com.moodylsis.moodbe;

import java.sql.Connection;

public interface SeriesInterface {
	// GET /v0/series/{seriesID}
	public String getJSON(Connection conn, int seriesID);
	
	// POST /v0/series
	// I didn't put events:list in because the series should be created before the events
	public int newSeries(Connection conn, String title, String desc, int hostID);
	
	// PUT /v0/series/{seriesID}
	public boolean newSeries(Connection conn, int seriesID, String newTitle, String newDesc);
	
	// DELETE /v0/series/{seriesID}
	public boolean deleteSeries(Connection conn, int seriesID);
	
	
	// there are other methods in the class diagram but I'm not sure if we need all of them, we can add them later if we do
	
}
