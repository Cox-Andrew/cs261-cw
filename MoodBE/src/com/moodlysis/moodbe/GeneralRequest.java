package com.moodlysis.moodbe;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GeneralRequest {
	
	public static String readJSON(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StringBuilder data = new StringBuilder();
		BufferedReader reader = request.getReader();
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				data.append(line).append('\n');
			}
		} finally {
			reader.close();
		}
		//TODO test content-type header is application/json ( request.getContentType() )
		String jsonData = data.toString();
		return jsonData;
	}
	
	public static int getIDFromPath(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] path = request.getRequestURI().split("/");
		int ID = Integer.parseInt(path[path.length - 1]);		
		return ID;
	}
	
	public static int getIDFromQuery(HttpServletRequest request, HttpServletResponse response, String value) throws ServletException, IOException {
		//only works for one query, needs further development and error handling
		String IDString = request.getParameter(value);
		int ID = Integer.parseInt(IDString);		
		return ID;
	}
	
	public static int[] extendArray(int[] array, int newSize) {
		int[] temp = new int[newSize];
		for (int i = 0; i < array.length && i < newSize; i++) {
			temp[i] = array[i];
		}
		return temp;
	}
	
	public static int getAttendeeIDFromAuthToken(HttpServletRequest request) {
		// TODO
		return -1;
	}
	
	public static int getHostIDFromAuthToken(HttpServletRequest request) {
		// TODO
		return -1;
	}

}
