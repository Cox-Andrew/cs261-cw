package com.moodlysis.moodbe.integrationinterfaces;

import java.sql.Connection;
import java.sql.SQLException;

public interface HostInterface {
	
	//  POST /v0/hosts	
	public int newHost(String name, String pass, String email);
	
	//  PUT /v0/hosts/{hostID}
	public boolean editHost(int hostID, String name, String pass, String email);
	

}
