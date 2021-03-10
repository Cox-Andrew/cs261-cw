package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.EventForm;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

/**
 * Servlet implementation class EventFormRequest
 */
@WebServlet(urlPatterns = {"/v0/event-forms", "/v0/event-forms/*"})
public class EventFormRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventFormRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
    public String getJSON(int eventFormID) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "questionID": 1 }
		 */
		
		JSONObject output = new JSONObject();
		output.put("eventFormID", eventFormID);
		return output.toJSONString();
	}
	
    @SuppressWarnings("unchecked")
	public String getJSON(EventForm.eventFormInfo info) {
		JSONObject output = new JSONObject();
		output.put("eventFormID", info.eventFormID);
		output.put("eventID", info.eventID);
		output.put("formID", info.formID);
		output.put("numInEvent", info.numInEvent);
		output.put("isActive", info.isActive);
		return output.toJSONString();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Catch /v0/event-forms/
		if (request.getRequestURI().matches("/v0/event-forms/([1-9])([0-9]*)")) {
			doGetEventForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doGetEventForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int eventFormID = GeneralRequest.getIDFromPath(request, response);
		
		Connection conn = DatabaseConnection.getConnection();
		EventForm eventForm = new EventForm(response.getWriter(), conn);
		
		EventForm.eventFormInfo info;
		try {
			info = eventForm.getEventForm(eventFormID);
		} catch (MoodlysisInternalServerError e) {
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
		int eventID = info.eventID;
		int formID = info.formID;
		int numInEvent = info.numInEvent;
		//Boolean isActive = info.isActive;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for isActive which isnt just false
		if (formID == -1 || eventID == -1 || numInEvent == -1) {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().equals("/v0/event-forms")) {
			doNewEventForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doNewEventForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int eventID = -1;
		int formID = -1;
		int precedingID = -1;
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 *  {
			 *  	"eventID": 1,
			 *		"formID": 1,
			 *		"preceding-eventFormID": 13 
			 *	}
			 *
			 * {"eventID": 1,"formID": 1,"preceding-eventFormID": 13 }
			 * 
			 * 
			 * JSON looks like the above
			 */
			precedingID = Integer.valueOf(postObject.get("preceding-eventFormID").toString());
			eventID = Integer.valueOf(postObject.get("eventID").toString());
			formID = Integer.valueOf(postObject.get("formID").toString());

		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		//CALL JDBC
		Connection conn = DatabaseConnection.getConnection();
		EventForm eventForm = new EventForm(response.getWriter(), conn);
		int eventFormID;
		try {
			eventFormID = eventForm.newEventForm(eventID, formID, precedingID, false);
		} catch (MoodlysisInternalServerError e) {
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
		if (eventFormID != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(eventFormID);
			response.getWriter().append(output + "\n");
		}
		else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/event-forms/([1-9])([0-9]*)")) {
			doEditEventForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doEditEventForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int eventFormID = GeneralRequest.getIDFromPath(request, response);
		int previousID = -1;
		Boolean isActive = false;
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
			/*
			 *  {
			 *		"preceding-eventFormID": 2 -- (-2 for no change)
			 *		"isActive": true
			 *	}
			 *
			 * {"preceding-eventFormID": 2, "isActive": true }
			 * 
			 * 
			 * JSON looks like the above
			 */
			if (putObject.get("isActive") != null) {
				isActive = Boolean.parseBoolean(putObject.get("isActive").toString());
			}
			if (putObject.get("preceding-eventFormID") != null) {
				previousID = Integer.parseInt(putObject.get("preceding-eventFormID").toString());
			}
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		
		Connection conn = DatabaseConnection.getConnection();
		EventForm eventForm = new EventForm(response.getWriter(), conn);
		try {
			eventForm.editEventForm(eventFormID, previousID, isActive);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (MoodlysisInternalServerError e) {
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
		
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getRequestURI().matches("/v0/event-forms/([1-9])([0-9]*)")) {
			doDeleteEventForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doDeleteEventForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = DatabaseConnection.getConnection();
		EventForm eventForm = new EventForm(response.getWriter(), conn);
		int eventFormID = GeneralRequest.getIDFromPath(request, response);
		
		try {
			eventForm.deleteEventForm(eventFormID);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (MoodlysisInternalServerError e) {
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
	}

}
