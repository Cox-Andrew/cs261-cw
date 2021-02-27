package com.moodlysis.moodbe;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Answer;
import com.moodlysis.moodbe.integration.Event;
import com.moodlysis.moodbe.requestexceptions.MoodlysisForbidden;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;

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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.getWriter().append("Invalid parse data");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		int attendeeID;
		int eventID;
		int eventFormID;
		int questionID;
		String questionResponse;
		boolean isAnonymous;
		
		try {
			
			attendeeID = (int) postObject.get("attendeeID");
			eventID = (int) postObject.get("eventID");
			eventFormID = (int) postObject.get("eventFormID");
			questionID = (int) postObject.get("eventFormID");
			JSONObject data = (JSONObject) postObject.get("data");
			questionResponse = (String) data.get("response");
			isAnonymous = (boolean) data.get("isAnonymous");
			
		} catch (Exception e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// get the current time
		LocalDateTime timeSubmitted = LocalDateTime.now();
		
		Answer answer = new Answer(response.getWriter());
		int answerID;
		
		try {
			answerID = answer.newAnswer(attendeeID, questionID, eventFormID, timeSubmitted, questionResponse, isAnonymous);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		
		JSONObject js = new JSONObject();
		js.put("answer", answerID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
	}
	
	

}
