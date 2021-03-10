package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.RegisterEvent;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

/**
 * Servlet implementation class registerevents
 */
@WebServlet(urlPatterns = {"/v0/invite-code", "/v0/register-event"})
public class RegisterEventsRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterEventsRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
    public String getJSON(String inviteCode) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "invite-code": aa8awe9bh }
		 */
		
		JSONObject output = new JSONObject();
		output.put("invite-code", inviteCode);
		return output.toJSONString();
	}
	
    @SuppressWarnings("unchecked")
	public String getJSON(RegisterEvent.registerInfo info) {
		JSONObject output = new JSONObject();
		output.put("eventID", info.eventID);
		//output.put("cookie", info.cookie);
		return output.toJSONString();
	}
    
    @SuppressWarnings("unchecked")
    public String getJSON(int[] returnIDs, String outName) {
    	JSONObject output = new JSONObject();
    	JSONArray list = new JSONArray();
    	if (returnIDs != null) {
	    	for (int i = 0; i < returnIDs.length; i++) {
	    		list.add(returnIDs[i]);
	    	}
    	}
    	output.put(outName, list);
    	return output.toJSONString();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//GET /v0/register-event?eventID={eventID}
		if (request.getRequestURI().equals("/v0/register-event") && request.getParameter("eventID") != null) {
			doGetAttendees(request, response);
		}
		//GET /v0/register-event?attendeeID={attendeeID}
		else if (request.getRequestURI().equals("/v0/register-event") && request.getParameter("attendeeID") != null) {
			doGetEvents(request, response);
		}
		//GET /v0/invite-code?eventID={eventID}
		else if (request.getRequestURI().equals("/v0/invite-code")) {
			doGetInviteCode(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doGetAttendees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int eventID = GeneralRequest.getIDFromQuery(request, response, "eventID");
		Connection conn = DatabaseConnection.getConnection();
		RegisterEvent registerEvent = new RegisterEvent(response.getWriter(), conn);

		int[] attendeeIDs;
		try {
			attendeeIDs = registerEvent.getAttendees(eventID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
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
		
		// tell the caller that this is JSON content (move to front)
		if (attendeeIDs == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		if (attendeeIDs.length == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(attendeeIDs, "attendeeIDs");
			response.getWriter().append(output + "\n");
		}
	}
	
	protected void doGetEvents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int attendeeID = GeneralRequest.getIDFromQuery(request, response, "attendeeID");
		Connection conn = DatabaseConnection.getConnection();
		RegisterEvent registerEvent = new RegisterEvent(response.getWriter(), conn);

		int[] eventIDs;
		try {
			eventIDs = registerEvent.getEvents(attendeeID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
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
		
		// tell the caller that this is JSON content (move to front)
		if (eventIDs == null) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		if (eventIDs.length == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(eventIDs, "eventIDs");
			response.getWriter().append(output + "\n");
		}
	}
	
	protected void doGetInviteCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int eventID = GeneralRequest.getIDFromQuery(request, response, "eventID");
		
		Connection conn = DatabaseConnection.getConnection();
		RegisterEvent registerEvent = new RegisterEvent(response.getWriter(), conn);

		String inviteCode;
		try {
			inviteCode = registerEvent.getInviteCode(eventID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
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
		
		// tell the caller that this is JSON content (move to front)
		if (inviteCode == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(inviteCode);
			response.getWriter().append(output + "\n");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getRequestURI().equals("/v0/register-event")) {
			doPostRegisterEvents(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doPostRegisterEvents(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		String inviteCode = null;
		int attendeeID = -1;
		RegisterEvent.registerInfo info;
		
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 *  {
			 *		"invite-code": "xcedag55a",
			 *		"attendeeID": 5452454
			 *	}
			 *
			 * {"invite-code": "xcedag55a", "attendeeID": 5452454}
			 * 
			 * JSON looks like the above
			 */
			inviteCode = postObject.get("invite-code").toString();
			attendeeID = Integer.valueOf(postObject.get("attendeeID").toString());
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		Connection conn = DatabaseConnection.getConnection();
		RegisterEvent registerEvent = new RegisterEvent(response.getWriter(), conn);

		try {
			info = registerEvent.register(inviteCode, attendeeID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
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
		
		//int eventID = info.eventID;
		//String cookie = info.cookie;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for cookie which isnt just empty string
		if (info.eventID == -1) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(info);
			response.getWriter().append(output + "\n");
		}
	}
	
			

}
