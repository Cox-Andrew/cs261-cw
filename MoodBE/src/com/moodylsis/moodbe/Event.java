package com.moodylsis.moodbe;

import java.io.IOException;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Event
 */
@WebServlet({"/v0/events", "/v0/events/*"})
public class Event extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Event() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String eventId;

		String pathInfo = request.getPathInfo();
		if (pathInfo == null) {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
		    return;
		}
		
		// always begins with a /.
		// return the sections after /v0/events.
		String[] pathParts = pathInfo.split("/");
		
		
		
		if (pathParts.length >= 2) {
			eventId = pathParts[1];
			
			// attempt to parse the eventID?
		
		
		
		}
		else {
			// return error
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		
		// GET /v0/events/{eventID}
		if (pathParts.length == 2) {
			// call something
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

}
