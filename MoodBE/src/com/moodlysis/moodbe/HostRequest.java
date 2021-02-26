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
import com.moodlysis.moodbe.integration.Series;


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
    
    public String getJSON(Host.hostInfo info) {
		JSONObject output = new JSONObject();
		output.put("hostID", info.hostID);
		output.put("email", info.email);
		output.put("pass", info.pass);
		output.put("account-name", info.account);
		return output.toJSONString();
	}
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if  /v0/hosts/{hostID}
		doGetHost(request, response);
	}
	
	protected void doGetHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Host host = new Host(response.getWriter());
		int hostID = GeneralRequest.getIDFromPath(request, response);
		
		Host.hostInfo info = host.getHost(hostID);
		String email = info.email;
		String pass = info.pass;
		String account = info.account;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for account-name which isnt just empty string
		if (email.equals("") || pass.equals("") /*|| account.equals("")*/) {
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
		if (request.getRequestURI().equals("/v0/hosts")) {
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
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if /v0/series/{seriesID}
		doEditHost(request, response);
	}
	
	protected void doEditHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Host host = new Host(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int hostID = GeneralRequest.getIDFromPath(request, response);
		String email = "";
		String pass = "";
		String account = "";
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"data": {
			 *			"title": "New series Title",
			 *			"description": "New description of Series."
			 *		}
			 *	}
			 *
			 * { "data": { "title": "New series Title", "description": "New description of Series." }}
			 * 
			 * JSON looks like the above
			 */
			email = putObject.get("email").toString();
			pass = putObject.get("pass").toString();
			account = putObject.get("account-name").toString();
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//return;
		}
		if (host.editHost(hostID, email, pass, account)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//assumes id not found
			//put into response body what was wrong (can be handled in jdbc)
		}
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if /v0/series/{seriesID}
		doDeleteHost(request, response);
	}
	
	protected void doDeleteHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Host host = new Host(response.getWriter());
		int hostID = GeneralRequest.getIDFromPath(request, response);
		
		if (host.deleteHost(hostID)) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			//assumes id not found
		}
	}

}
