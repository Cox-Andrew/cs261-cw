package com.moodlysis.moodbe;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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


@WebServlet({"/v0/events", "/v0/events/*"})
public class EventRequest extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventRequest() {
        super();
    }
    
    @SuppressWarnings("unchecked")
	private String getJSON(Event.EventInfo info) {
    	
    	/*{
			"eventID": 1243214,
			"seriesID" : 2342341,
			"formIDs": [12423142,4324324,5462354],
			"eventFormIDs": [312312,312312,2352234], 
			"data": {
				"title": "Event Title",
				"description": "Description of Event."
			}
		}*/
    	
		JSONObject output = new JSONObject();
		
		output.put("eventID", info.eventID);
		output.put("seriesID", info.seriesID);
		
		JSONArray formIDs = new JSONArray();
		for (int i : info.formIDs) formIDs.add(i);
		output.put("formIDs", formIDs);
		
		JSONArray eventFormIDs = new JSONArray();
		for (int i : info.eventFormIDs) eventFormIDs.add(i);
		output.put("eventFormIDs", eventFormIDs);
		
		JSONObject data = new JSONObject();
		data.put("title", info.title);
		data.put("description", info.description);

		return output.toJSONString();
		
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		int l = request.getPathInfo().split("/").length;
		

		
		System.out.println(request.getPathInfo());
		if (request.getPathInfo().length() == 1/*slash only*/) {
			doGetEventsWithSeriesID(request, response);
			return;
		} else if (!request.getRequestURI().matches("/v0/events/([1-9])([0-9]*)")) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND, "must be of form /v0/events?seriesID={} or /v0/events/{eventID}");
		    return;
		}
		

		// GET /v0/series/{seriesID}
		int eventID = GeneralRequest.getIDFromPath(request, response);
		
		Event event = new Event(response.getWriter());
		Event.EventInfo eventInfo;
		
		try {
			eventInfo = event.getEventInfo(eventID);
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} catch (MoodlysisInternalServerError e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		}
		
		String responseJSON = getJSON(eventInfo);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(responseJSON);
		
	}
	
	@SuppressWarnings("unchecked")
	private void doGetEventsWithSeriesID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String seriesIDString = request.getParameter("seriesID");
		if (seriesIDString == null) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "missing ?seriesID={}");
		    return;
		}
		
		int seriesID;
		
		// parse eventID
		try {
			seriesID = Integer.parseInt(seriesIDString);
		} catch (NumberFormatException e) {
			response.getWriter().print("unable to parse attendeeID");
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		LinkedList<Integer> eventIDs;
		Event event = new Event(response.getWriter());
		
		try {
			eventIDs = event.getSeriesEvents(seriesID);
		} catch (MoodlysisBadRequest e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			response.getWriter().print(e.toString());
			return;
		} catch (MoodlysisInternalServerError e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		}
		
		JSONObject output = new JSONObject();
		JSONArray eventIDsJSONArr = new JSONArray();
		for (int eventID : eventIDs) {
			eventIDsJSONArr.add(eventID);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		output.put("eventIDs", eventIDsJSONArr);
		response.getWriter().print(output.toJSONString());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (!request.getRequestURI().equals("/v0/events")) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "not of form /v0/events");
			return;
		}
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "unable to parse: " + e.toString());
			return;
		}
		
		int seriesID;
		String title;
		String description;
		LocalDateTime timeStart;
		LocalDateTime timeEnd;
		
		try {
			seriesID = (Integer) postObject.get("seriesID");
			JSONObject data = (JSONObject) postObject.get("data");
			title = (String) data.get("title");
			description = (String) data.get("description");
			timeStart = LocalDateTime.parse((String) data.get("timeStart"));
			timeEnd = LocalDateTime.parse((String) data.get("timeEnd"));
			
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		
		// TODO - uncomment when getHostIDFromAuthToken is implemented
		int hostIDAuth = 0;
//		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
		
		if (hostIDAuth == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as a host");
			return;
		}
		
		Event event = new Event(response.getWriter());
		int eventID;
		
		try {
			eventID = event.newEvent(seriesID, title, description, timeStart, timeEnd, hostIDAuth);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		}
		
		
		JSONObject js = new JSONObject();
		js.put("eventID", eventID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "must be of form /v0/events/{eventID}");
		    return;
		}
		
		int eventID = GeneralRequest.getIDFromPath(request, response);
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse. " + e.toString());
			return;
		}
		
		String title;
		String description;
		LocalDateTime timeStart;
		LocalDateTime timeEnd;
		
		try {
			JSONObject data = (JSONObject) postObject.get("data");
			title = (String) data.get("title");
			description = (String) data.get("description");
			timeStart = LocalDateTime.parse((String) data.get("timeStart"));
			timeEnd = LocalDateTime.parse((String) data.get("timeEnd"));
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		// TODO - uncomment when getHostIDFromAuthToken is implemented
		int hostIDAuth = 0;
//		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
		
		if (hostIDAuth == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "not signed in as a host");
			return;
		}
		
		
		Event event = new Event(response.getWriter());
		try {
			event.editEvent(eventID, title, description, timeStart, timeEnd, hostIDAuth);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		}
		
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND, "must be of form /v0/events/{eventID}");
		    return;
		}
		
		int eventID = GeneralRequest.getIDFromPath(request, response);
		
		// TODO
		int hostIDAuth = 0;
//		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
		
		if (hostIDAuth == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as a host");
			return;
		}
		
		Event event = new Event(response.getWriter());
		try {
			event.deleteEvent(eventID, hostIDAuth);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.getWriter().print(e.toString());
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		}
		
		
	}

}
