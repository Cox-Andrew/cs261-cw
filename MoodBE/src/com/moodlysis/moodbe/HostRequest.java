package com.moodlysis.moodbe;

import java.io.IOException;
import java.io.BufferedReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Host;
import com.moodlysis.moodbe.GeneralRequest;

/**
 * Servlet implementation class HostRequest
 */
@WebServlet(urlPatterns = {"/v0/hosts", "/v0/hosts/*"})
public class HostRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HostRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String getJSON(int hostID) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "hostID": 1 }
		 */
		
		JSONObject output = new JSONObject();
		output.put("hostID", hostID);
		return output.toJSONString();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getServletPath().equals("/v0/hosts")) {
			doNewHost(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	protected void doNewHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Host host = new Host(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		BufferedReader reader = request.getReader();
		String email = "";
		String pass = "";
		String account = "";
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 *  {
			 *   	"email": "example@gmail.com",
			 *		"pass": "mypassword",
			 *		"account-name": "Joe Bloggs"
			 *	}
			 *
			 * { "email": "example@gmail.com", "pass": "mypassword", "account-name": "Joe Bloggs"}
			 * 
			 * JSON looks like the above
			 */
			email = postObject.get("email").toString();
			pass = postObject.get("pass").toString();
			account = postObject.get("account-name").toString();
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
		}
		//CALL JDBC
		int hostID = host.newHost(email, pass, account);
		
		// tell the caller that this is JSON content (move to front)
		if (hostID != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(hostID);
			response.getWriter().append(output + "\n");
		}
		else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
