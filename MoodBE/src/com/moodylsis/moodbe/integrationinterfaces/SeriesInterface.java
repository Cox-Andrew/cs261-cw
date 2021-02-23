package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface SeriesInterface {
	// GET /v0/series/{seriesID}
	public String getJSON(Connection conn, int seriesID) throws SQLException;
	
	// POST /v0/series
	// I didn't put events:list in because the series should be created before the events
	public int newSeries(Connection conn, String title, String desc, int hostID) throws SQLException;
	
	// PUT /v0/series/{seriesID}
	public boolean newSeries(Connection conn, int seriesID, String newTitle, String newDesc) throws SQLException;
	
	// DELETE /v0/series/{seriesID}
	public boolean deleteSeries(Connection conn, int seriesID) throws SQLException;
	
	
	// there are other methods in the class diagram but I'm not sure if we need all of them, we can add them later if we do
	
}
