package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;

import com.moodlysis.moodbe.integrationinterfaces.SeriesInterface;

public class Series implements SeriesInterface {

	@Override
	public String getJSON(Connection conn, int seriesID) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int newSeries(Connection conn, String title, String desc, int hostID) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean newSeries(Connection conn, int seriesID, String newTitle, String newDesc) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteSeries(Connection conn, int seriesID) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
