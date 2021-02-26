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
		String[] path = request.getPathTranslated().split("/");
		int ID = Integer.valueOf(path[path.length - 1]);		
		return ID;
	}

}
