package com.moodylsis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface RegisterEventInterface {
	
	// POST /v0/register-event
	public int getEventIDOfInviteCode(Connection conn, String inviteCode) throws SQLException;

}
