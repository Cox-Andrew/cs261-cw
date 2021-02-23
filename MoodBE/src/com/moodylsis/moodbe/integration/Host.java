package com.moodylsis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;

import com.moodylsis.moodbe.integrationinterfaces.HostInterface;

public class Host implements HostInterface {

	@Override
	public int newHost(Connection conn, String name, String pass, String email) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editHost(Connection conn, int hostID, String name, String pass, String email) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
