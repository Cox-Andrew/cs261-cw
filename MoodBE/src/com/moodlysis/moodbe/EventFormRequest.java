package com.moodlysis.moodbe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.EventForm;
import com.moodlysis.moodbe.integration.Question;

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
		// TODO Auto-generated method stub
		//if  /v0/series/{seriesID}
		doGetEventForm(request, response);
	}
	
	protected void doGetEventForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EventForm eventForm = new EventForm(response.getWriter());
		int eventFormID = GeneralRequest.getIDFromPath(request, response);
		
		EventForm.eventFormInfo info = eventForm.getEventForm(eventFormID);
		int eventID = info.eventID;
		int formID = info.formID;
		int numInEvent = info.numInEvent;
		Boolean isActive = info.isActive;
		
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
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
