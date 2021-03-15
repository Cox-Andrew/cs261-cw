package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
//import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Attendee;
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


@WebServlet({"/v0/attendees", "/v0/attendees/*"})
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
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "must be of form /v0/attendee/{attendeeID}");
		    return;
		}
		
		int attendeeID = GeneralRequest.getIDFromPath(request, response);
		
		Connection conn = DatabaseConnection.getConnection();
		Attendee attendee = new Attendee(response.getWriter(), conn);
		Attendee.AttendeeInfo attendeeInfo;
		
		// extract attendeeID from cookie TODO
		int verificationAttendeeID = 0;
//		int verificationAttendeeID = GeneralRequest.getAttendeeIDFromAuthToken(request);
		if (verificationAttendeeID == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as an attendee");
			return;
		}
		
		try {
			attendeeInfo = attendee.getAttendeeInfo(attendeeID, verificationAttendeeID);
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} catch (MoodlysisInternalServerError e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "POST must be of form /v0/attendees");
			return;
		}
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse data: " + e.toString());
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		Connection conn = DatabaseConnection.getConnection();
		Attendee attendee = new Attendee(response.getWriter(), conn);
		int attendeeID;
		
		try {
			attendeeID = attendee.newAttendee(accountName, pass, email, null);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisBadRequest e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
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
		js.put("attendeeID", attendeeID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		

		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "must be of form /v0/attendee/{attendeeID}");
		    return;
		}

		int attendeeID = GeneralRequest.getIDFromPath(request, response);
		

		// extract attendeeID from cookie TODO
		int verificationAttendeeID = 0;
//		int verificationAttendeeID = GeneralRequest.getAttendeeIDFromAuthToken(request);
		if (verificationAttendeeID == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as an attendee");
			return;
		}
		
		
		// read json
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		
		try {
			postObject = (JSONObject) postParser.parse(jsonData); 
		} catch(ParseException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse data: " + e.toString());
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		
		Connection conn = DatabaseConnection.getConnection();
		Attendee attendee = new Attendee(response.getWriter(), conn);

		
		try {
			// can either change account name only, or change everything
			if (email == null && pass == null && accountName != null) {
				attendee.editAttendeeName(attendeeID, accountName, verificationAttendeeID);
			} else if (email != null && pass != null && accountName != null) {
				attendee.editAttendee(attendeeID, accountName, pass, email, verificationAttendeeID);
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Must either include only account name, or all of accountName, email and password.");
				return;
			}
		} catch (MoodlysisInternalServerError e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
		return;
		
		
	}
	
	@Override 
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null || pathInfo.split("/").length != 2) {
		    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "must be of form /v0/attendee/{attendeeID}");
		    return;
		}

		int attendeeID = GeneralRequest.getIDFromPath(request, response);
		


		// extract attendeeID from cookie TODO
		int verificationAttendeeID = 0;
//		int verificationAttendeeID = GeneralRequest.getAttendeeIDFromAuthToken(request);
		if (verificationAttendeeID == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as an attendee");
			return;
		}
		
		Connection conn = DatabaseConnection.getConnection();
		Attendee attendee = new Attendee(response.getWriter(), conn);
		
		try {
			// can either change account name only, or change everything
			attendee.deleteAttendee(attendeeID, verificationAttendeeID);
		} catch (MoodlysisInternalServerError e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisNotFound e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} catch (MoodlysisForbidden e) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, e.toString());
			return;
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return;
		
	}

}
