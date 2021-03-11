package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.AttendeeTempSignIn;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

/**
 * Servlet implementation class AttendeeTempSignInRequest
 */
@WebServlet("/v0/attendee-temp-sign-in")
public class AttendeeTempSignInRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AttendeeTempSignInRequest() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		JSONObject postObject;
		String requestEmail;
		String requestPassword;
		
		// Parse request
		try {
			postObject = (JSONObject) postParser.parse(jsonData);
			requestEmail = (String) postObject.get("email");
			requestPassword = (String) postObject.get("pass");
		} catch(ParseException e) {
			System.out.println("Unable to parse. " + e.toString());
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to parse. " + e.toString());
			return;
		}
		
		// JDBC
		Connection conn = DatabaseConnection.getConnection();
		AttendeeTempSignIn a = new AttendeeTempSignIn(response.getWriter(), conn);
		int attendeeID;
		try {
			attendeeID = a.getAttendeeIDFromEmailAndPassword(requestEmail, requestPassword);
		} catch (MoodlysisNotFound e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_NOT_FOUND, e.toString());
			return;
		} catch (MoodlysisInternalServerError e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		}
		
		// Jsonify output
		JSONObject js = new JSONObject();
		js.put("attendeeID", attendeeID);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(js.toJSONString());
		
		
		
	}

}
