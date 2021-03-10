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
import org.json.simple.JSONArray;

import com.moodlysis.moodbe.integration.Feedback;
import com.moodlysis.moodbe.integration.Form;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

/**
 * Servlet implementation class FormRequest
 */
@WebServlet(urlPatterns = {"/v0/forms", "/v0/forms/*","/v0/forms?hostID=*"})
public class FormRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FormRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @SuppressWarnings("unchecked")
    public String getJSON(int formID) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "seriesID": 1 }
		 */
		
		JSONObject output = new JSONObject();
		output.put("formID", formID);
		return output.toJSONString();
	}
    
    @SuppressWarnings("unchecked")
    public String getJSON(Form.formInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray questions = new JSONArray();
		output.put("formID", info.formID);
		output.put("hostID", info.hostID);
		data.put("title", info.title);
		data.put("description", info.description);
		output.put("data", data);
		if (info.questionIDs != null) {
			for (int i = 0; i < info.questionIDs.length; i++) {
	    		questions.add(info.questionIDs[i]);
	    	}
		}
		output.put("questionIDs", questions);
		return output.toJSONString();
	}
    
    @SuppressWarnings("unchecked")
    public String getJSON(int[] formIDs) {
    	JSONObject output = new JSONObject();
    	JSONArray forms = new JSONArray();
    	if (formIDs != null) {
	    	for (int i = 0; i < formIDs.length; i++) {
	    		forms.add(formIDs[i]);
	    	}
    	}
    	output.put("formIDs", forms);
    	return output.toJSONString();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/forms/([0-9]*)")) {
			doGetForm(request, response);
		}
		else if (request.getRequestURI().equals("/v0/forms")) {
			doGetHostForms(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doGetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO get the question ids for the form
		Connection conn = DatabaseConnection.getConnection();
		Form form = new Form(response.getWriter(), conn);
		int formID = GeneralRequest.getIDFromPath(request, response);
		Form.formInfo info;
		try {
			info = form.getForm(formID);
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
		
		int hostID = info.hostID;
		String title = info.title;
		//String description = info.description;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for description which isnt just empty string
		if (hostID == -1 || title.equals("") /*|| description.equals("")*/) {
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
	
	protected void doGetHostForms(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int hostID = GeneralRequest.getIDFromQuery(request, response, "hostID");
		
		Connection conn = DatabaseConnection.getConnection();
		Form form = new Form(response.getWriter(), conn);

		int[] formIDs;
		try {
			formIDs = form.getFormIDsForHost(hostID);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
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
		if (formIDs == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		else if (formIDs.length == 0) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(formIDs);
			response.getWriter().append(output + "\n");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().equals("/v0/forms")) {
			doNewForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	protected void doNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int hostID = -1;
		String title = "";
		String description = "";
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"hostID": 1,
			 *		"data": {
			 *			"title": "Form title",
			 *			"description": "Form description"
			 *		}
			 *	}
			 *
			 * {"hostID": 1,"data": {"title": "Form title","description": "Form description"}}
			 * 
			 * JSON looks like the above
			 */
			hostID = Integer.valueOf(postObject.get("hostID").toString());
			title = ((JSONObject) postObject.get("data")).get("title").toString();
			description = ((JSONObject) postObject.get("data")).get("description").toString();
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		//CALL JDBC
		Connection conn = DatabaseConnection.getConnection();
		Form form = new Form(response.getWriter(), conn);
		int formID;
		try {
			formID = form.newForm(hostID, title, description);
		} catch (MoodlysisInternalServerError e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.toString());
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
		if (formID != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(formID);
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
		if (request.getRequestURI().matches("/v0/forms/([1-9])([0-9]*)")) {
			doEditForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int formID = GeneralRequest.getIDFromPath(request, response);
		String title = "";
		String description = "";
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"data": {
			 *			"title": "New form title",
			 *			"description": "New form description"
			 *		}
			 *	}
			 *
			 * {"data": {"title": "New form title","description": "New form description"}}
			 * 
			 * JSON looks like the above
			 */
			title = ((JSONObject) putObject.get("data")).get("title").toString();
			description = ((JSONObject) putObject.get("data")).get("description").toString();
		} catch(ParseException e) {
			//TODO
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.toString());
			return;
		}
		Connection conn = DatabaseConnection.getConnection();
		Form form = new Form(response.getWriter(), conn);
		try {
			form.editForm(formID, title, description);
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


	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/forms/([1-9])([0-9]*)")) {
			doDeleteForm(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	protected void doDeleteForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int formID = GeneralRequest.getIDFromPath(request, response);
		
		Connection conn = DatabaseConnection.getConnection();
		Form form = new Form(response.getWriter(), conn);
		try {
			form.deleteForm(formID);
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
