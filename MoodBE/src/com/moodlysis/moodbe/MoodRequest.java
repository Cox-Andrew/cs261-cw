package com.moodlysis.moodbe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.moodlysis.moodbe.integration.Answer;
import com.moodlysis.moodbe.integration.Feedback;
import com.moodlysis.moodbe.integration.Mood;
import com.moodlysis.moodbe.integrationinterfaces.MoodInterface.MoodListInfo;
import com.moodlysis.moodbe.requestexceptions.*;



@WebServlet({"/v0/moods", "/v0/moods/*"})
public class MoodRequest extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoodRequest() {
        super();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String eventIDString = request.getParameter("eventID");
		String attendeeIDString = request.getParameter("attendeeID");
		String timeUpdatedSinceString = request.getParameter("time-updated-since");
		
		int eventID;
		
		// must include eventID
		if (eventIDString == null) {
			response.getWriter().print("must include ?eventID=...");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		// parse eventID
		try {
			eventID = Integer.parseInt(eventIDString);
		} catch (NumberFormatException e) {
			response.getWriter().print("unable to parse attendeeID");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		
		Mood mood = new Mood(response.getWriter());
		Mood.MoodListInfo moodListInfo = null;
		
		if (attendeeIDString == null && timeUpdatedSinceString != null) {
			LocalDateTime timeUpdatedSince;

			try {
				timeUpdatedSince = LocalDateTime.parse(timeUpdatedSinceString);
			} catch (DateTimeParseException e) {
				response.getWriter().print("invalid timestamp format. Do something like this: 2007-12-03T10:15:30, or anything else that can be parsed by LocalDateTime.parse");
				response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
			
			// TODO authentication
			
			try {
				moodListInfo = mood.getMoodSince(eventID, timeUpdatedSince);
			} catch (MoodlysisInternalServerError e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
				return;
			}	
			
			String responseJSON = getMoodListJSON(moodListInfo);
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(responseJSON);
			
		}
		
		response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
	
	}


	@SuppressWarnings("unchecked")
	private String getMoodListJSON(MoodListInfo info) {
		JSONObject output = new JSONObject();
		output.put("eventID", info.eventID);
		output.put("time-submitted-since", info.timeSubmittedSince);
		output.put("contains", info.list.size());
		JSONArray list = new JSONArray();
		for (Mood.MoodInfo moodInfo: info.list) {
			JSONObject moodObj = new JSONObject();
			moodObj.put("time-submitted", moodInfo.timeSubmitted.toString());
			moodObj.put("mood-value", moodInfo.moodValue);
			moodObj.put("answerID", moodInfo.answerID);
			moodObj.put("attendeeID", moodInfo.attendeeID);
			list.add(moodObj);
		}
		output.put("list", list);
		return output.toJSONString();
	}


	

}
