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

import com.moodlysis.moodbe.integration.Host;
import com.moodlysis.moodbe.requestexceptions.MoodlysisBadRequest;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;


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
    
    @SuppressWarnings("unchecked")
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
    
    @SuppressWarnings("unchecked")
    public String getJSON(Host.hostInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
//		output.put("hostID", info.hostID);
		data.put("email", info.email);
//		output.put("pass", info.pass);
		data.put("account-name", info.account);
		output.put("data", data);
		return output.toJSONString();
	}
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/hosts/([1-9])([0-9]*)")) {
			doGetHost(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doGetHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int hostID = GeneralRequest.getIDFromPath(request, response);
		Connection conn = DatabaseConnection.getConnection();
		Host host = new Host(response.getWriter(), conn);
		Host.hostInfo info;
		try {
			info = host.getHost(hostID);
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
		String email = info.email;
		String pass = info.pass;
		//String account = info.account;
		
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
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
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
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		//CALL JDBC
		Connection conn = DatabaseConnection.getConnection();
		Host host = new Host(response.getWriter(), conn);
		int hostID;
		try {
			hostID = host.newHost(email, pass, account);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
			return;
		} catch (MoodlysisBadRequest e){
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
		if (request.getRequestURI().matches("/v0/hosts/([1-9])([0-9]*)")) {
			doEditHost(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doEditHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int hostID = GeneralRequest.getIDFromPath(request, response);
		String email = "";
		String pass = "";
		String account = "";
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
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
			email = putObject.get("email").toString();
			pass = putObject.get("pass").toString();
			account = putObject.get("account-name").toString();
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		Connection conn = DatabaseConnection.getConnection();
		Host host = new Host(response.getWriter(), conn);
		try {
			host.editHost(hostID, email, pass, account);
			response.setStatus(HttpServletResponse.SC_OK);
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
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/hosts/([1-9])([0-9]*)")) {
			doDeleteHost(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doDeleteHost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int hostID = GeneralRequest.getIDFromPath(request, response);
		Connection conn = DatabaseConnection.getConnection();
		Host host = new Host(response.getWriter(), conn);
		try {
			host.deleteHost(hostID);
			response.setStatus(HttpServletResponse.SC_OK);
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
	}

}
