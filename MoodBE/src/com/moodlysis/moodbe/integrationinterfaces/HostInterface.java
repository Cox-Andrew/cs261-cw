package com.moodlysis.moodbe.integrationinterfaces;

import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public interface HostInterface {
	
	public static class hostInfo {
		public int hostID;
		public String email;
		public String pass;
		public String account;
	}
	
	//  GET /v0/hosts/{hostID}
	public hostInfo getHost(int hostID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	//  POST /v0/hosts	
	public int newHost(String name, String pass, String email) throws MoodlysisInternalServerError;
	
	//  PUT /v0/hosts/{hostID}
	public boolean editHost(int hostID, String name, String pass, String email) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
	//  DELETE /v0/hosts/{hostID}
	public boolean deleteHost(int hostID) throws MoodlysisInternalServerError, MoodlysisNotFound;
	
}
