package com.moodlysis.moodbe;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.moodlysis.moodbe.integration.Event;

// GET /v0/events/{eventID}
// GET /v0/events/{eventID}/invite-code
// GET /v0/event/{eventID}/analytics
// POST /v0/events
// POST /v0/events/{eventID}/forms
// PUT /v0/events/{eventID}
// DELETE /v0/events/{eventID}


@WebServlet({"/v0/events", "/v0/events/*"})
public class EventRequest extends HttpServlet {
	
	private static Event event = new Event();
	private Connection conn;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventRequest() {
        super();
        
        conn = DatabaseConnection.getConnection();
        
        // get database connection
        
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int eventID;

		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		// always begins with a /.
		// return the sections after /v0/events.
		String[] pathParts = pathInfo.split("/");
		
		
		
		if (pathParts.length >= 2) {
			
			String eventIDString = pathParts[1];
			
			// parse the eventID to int
			try {
				eventID = Integer.parseInt(eventIDString);
			} catch (NumberFormatException e) {
			    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
				return;
			}
		
		}
		else {
			// return error
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		
		// GET /v0/events/{eventID}
		if (pathParts.length == 2) {
			
			String eventJSON;
			
			try {
				/* this is where the next layer gets called */
				eventJSON = event.getJSON(conn, eventID);
				
			} catch (SQLException e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			
			if (eventJSON == null) {
				// event not found
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			
			response.getWriter().append(eventJSON);
			return;
		}
		
		else if (pathParts.length == 3) {
			switch (pathParts[2]) {
			
			// GET /v0/events/{eventID}/invite-code
			case "invite-code":
				// TODO
				return;
				
			// GET /v0/event/{eventID}/analytics
			case "analytics":
				// TODO
				return;
			}
		}
		
		// nothing was matched - error
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());

		// POST /v0/events
		// POST /v0/events/{eventID}/forms
		
		String eventId;
		String pathInfo = request.getPathInfo();
		BufferedReader requestPayload = request.getReader();
		
		// POST /v0/events
		if (pathInfo == null) {
			// call something
			return;
		}
		
		String[] pathParts = pathInfo.split("/");
		
		// POST /v0/events/{eventID}/forms
		if (pathParts.length == 3) {
			String eventID = pathParts[1];
			// call
			return;
		}
		
		
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
		
		
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		BufferedReader requestPayload = request.getReader();
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		String[] pathParts = pathInfo.split("/");
		
		// PUT /v0/events/{eventID}
		if (pathParts.length == 2) {
			String eventID = pathParts[1];
			// call
			return;
		}
		
		
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
		
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		String[] pathParts = pathInfo.split("/");
		
		// DELETE /v0/events/{eventID}
		if (pathParts.length == 2) {
			String eventID = pathParts[1];
			// call
			return;
		}
		
	    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return;
	}

}
