package com.moodlysis.moodbe.integration;

import java.sql.Connection;
import java.sql.SQLException;

import com.moodlysis.moodbe.integrationinterfaces.RegisterEventInterface;

public class RegisterEvent implements RegisterEventInterface {

	@Override
	public int getEventIDOfInviteCode(Connection conn, String inviteCode) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
