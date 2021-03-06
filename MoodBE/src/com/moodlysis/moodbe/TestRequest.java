package com.moodlysis.moodbe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Trying to figure out how authentication will work.
 * 
 */
@WebServlet("/test")
public class TestRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public TestRequest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int attendeeID = GeneralRequest.getAttendeeIDFromAuthToken(request);
		int hostID = GeneralRequest.getHostIDFromAuthToken(request);
		response.getWriter().print(""
			+ "attendeeID: " + attendeeID + "\n"
			+ "hostID: " + hostID + "\n"	
		);
		
	}



}
