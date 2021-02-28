package com.moodlysis.moodbe;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Attendee;
import com.moodlysis.moodbe.integration.Event;
import com.moodlysis.moodbe.requestexceptions.*;

// GET /v0/events/{eventID}
// GET /v0/events/{eventID}/invite-code
// GET /v0/event/{eventID}/analytics
// POST /v0/events
// POST /v0/events/{eventID}/forms
// PUT /v0/events/{eventID}
// DELETE /v0/events/{eventID}

// connect to database in command line:
// go into docker, start the database containter
// open a cli in the docker, add the following command:
// psql -U mooduser mood 


@WebServlet({"/v0/atttendees", "/v0/atttendees/*"})
public class AttendeeRequest extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendeeRequest() {
        super();
    }
    
    @SuppressWarnings("unchecked")
	private String getJSON(Attendee.AttendeeInfo info) {
    	
    	/*{
			"data": {
				"email": "example@gmail.com",
				"account-name": "Joe Bloggs"
			}
		}*/
    	
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		data.put("email", info.email);
		data.put("account-name", info.accountName);
		output.put("data", data);

		return output.toJSONString();
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		// GET /v0/series/{seriesID}
		if (request.getPathInfo().split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		int attendeeID = GeneralRequest.getIDFromPath(request, response);
		
		Attendee attendee = new Attendee(response.getWriter());
		Attendee.AttendeeInfo attendeeInfo;
		
		// extract attendeeID from cookie TODO
		int verificationAttendeeID = 0;
//		int verificationAttendeeID = GeneralRequest.getAttendeeIDFromAuthToken(request);
		if (verificationAttendeeID == -1) {
			response.getWriter().append("Not signed in as an attendee");
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		try {
			attendeeInfo = attendee.getAttendeeInfo(attendeeID, verificationAttendeeID);
		} catch (MoodlysisNotFound e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		} catch (MoodlysisInternalServerError e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		} catch (MoodlysisForbidden e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		
		String responseJSON = getJSON(attendeeInfo);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(responseJSON);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!request.getRequestURI().equals("/v0/attendees")) {
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
		
		String email;
		String pass;
		String accountName;
		
		try {
			email = (String) postObject.get("email");
			pass = (String) postObject.get("pass");
			accountName = (String) postObject.get("account-name");
			
		} catch (Exception e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		
		
		Attendee attendee = new Attendee(response.getWriter());
		int attendeeID;
		
		try {
			attendeeID = attendee.newAttendee(accountName, pass, email, null);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		
		JSONObject js = new JSONObject();
		js.put("attendeeID", attendeeID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO
//		if (request.getPathInfo().split("/").length != 2) {
//		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
//		    return;
//		}
//		
//		int eventID = GeneralRequest.getIDFromPath(request, response);
//		
//		String jsonData = GeneralRequest.readJSON(request, response);
//		JSONParser postParser = new JSONParser();
//		JSONObject postObject;
//		
//		try {
//			postObject = (JSONObject) postParser.parse(jsonData); 
//		} catch(ParseException e) {
//			response.getWriter().append("Invalid parse data");
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//			return;
//		}
//		
//		String title;
//		String description;
//		LocalDateTime timeStart;
//		LocalDateTime timeEnd;
//		
//		try {
//			JSONObject data = (JSONObject) postObject.get("data");
//			title = (String) data.get("title");
//			description = (String) data.get("description");
//			timeStart = LocalDateTime.parse((String) data.get("timeStart"));
//			timeEnd = LocalDateTime.parse((String) data.get("timeEnd"));
//		} catch (Exception e) {
//			response.getWriter().print(e.toString());
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//			return;
//		}
//		
//		// TODO - uncomment when getHostIDFromAuthToken is implemented
//		int hostIDAuth = 0;
////		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
//		
//		if (hostIDAuth == -1) {
//			response.getWriter().append("Not signed in as a host");
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//			return;
//		}
//		
//		
//		Event event = new Event(response.getWriter());
//		try {
//			event.editEvent(eventID, title, description, timeStart, timeEnd, hostIDAuth);
//		} catch (MoodlysisInternalServerError e){
//			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return;
//		} catch (MoodlysisForbidden e) {
//			response.getWriter().print(e.toString());
//			response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			return;
//		}
		
		
	}
	
	@Override 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO
//		if (request.getPathInfo().split("/").length != 2) {
//		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
//		    return;
//		}
//		
//		int eventID = GeneralRequest.getIDFromPath(request, response);
//		
//		// TODO
//		int hostIDAuth = 0;
////		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
//		
//		if (hostIDAuth == -1) {
//			response.getWriter().append("Not signed in as a host");
//			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
//			return;
//		}
//		
//		Event event = new Event(response.getWriter());
//		try {
//			event.deleteEvent(eventID, hostIDAuth);
//		} catch (MoodlysisInternalServerError e){
//			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return;
//		} catch (MoodlysisForbidden e) {
//			response.getWriter().print(e.toString());
//			response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			return;
//		}
//		
		
	}

}
