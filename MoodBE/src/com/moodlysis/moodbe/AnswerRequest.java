package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
//import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Answer;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

@WebServlet({"/v0/answers", "/v0/answers/*"})
public class AnswerRequest extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 /**
     * @see HttpServlet#HttpServlet()
     */
    public AnswerRequest() {
        super();
    }
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!request.getRequestURI().equals("/v0/answers")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "url not of form /v0/answers");
			return;
		}
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parse data: " + e.toString());
			return;
		}
		
		int attendeeID;
		//int eventID;
		int eventFormID;
		int questionID;
		String questionResponse;
		boolean isAnonymous;
		
		try {
			
			attendeeID = (int) Integer.parseInt(postObject.get("attendeeID").toString());
			//eventID = (int)Integer.parseInt(postObject.get("eventID").toString());
			eventFormID = (int) Integer.parseInt(postObject.get("eventFormID").toString());
			questionID = (int) Integer.parseInt(postObject.get("questionID").toString());
			JSONObject data = (JSONObject) postObject.get("data");
			questionResponse = (String) data.get("response").toString();
			isAnonymous = (boolean) Boolean.parseBoolean(data.get("isAnonymous").toString());
			
		} catch (Exception e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "error on parsing json" + e.toString());
			return;
		}
		
		// get the current time
		Instant now = Instant.now();
		//LocalDateTime timeSubmitted = LocalDateTime.now();
		
		Connection conn = DatabaseConnection.getConnection();
		Answer answer = new Answer(response.getWriter(), conn);
		int answerID;
		
		try {
			answerID = answer.newAnswer(attendeeID, questionID, eventFormID, now, questionResponse, isAnonymous);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		JSONObject js = new JSONObject();
		js.put("answerID", answerID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!request.getRequestURI().matches("/v0/answers/([0-9]*)")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "url not of form /v0/answers/{answerID}");
			return;
		}
		int answerID = GeneralRequest.getIDFromPath(request, response);
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		JSONObject putObject;
		
		try {
			putObject = (JSONObject) putParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parse data: " + e.toString());
			return;
		}
		
		String questionResponse;
		boolean isAnonymous;
		
		try {
			
			
			JSONObject data = (JSONObject) putObject.get("data");
			questionResponse = (String) data.get("response").toString();
			isAnonymous = (boolean) Boolean.parseBoolean(data.get("isAnonymous").toString());
			
		} catch (Exception e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "error on parsing json" + e.toString());
			return;
		}
		
		// get the current time
		Instant now = Instant.now();
		
		Connection conn = DatabaseConnection.getConnection();
		Answer answer = new Answer(response.getWriter(), conn);
		
		
		try {
			if (answer.editAnswer(answerID, now, isAnonymous, questionResponse)) {
				
			}
			
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e){
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

}
