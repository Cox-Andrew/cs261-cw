package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface HostInterface {
	
	//  POST /v0/hosts	
	public int newHost(Connection conn, String name, String pass, String email) throws SQLException;
	
	//  PUT /v0/hosts/{hostID}
	public boolean editHost(Connection conn, int hostID, String name, String pass, String email) throws SQLException;
	

}
