package com.moodlysis.moodbe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;

import com.moodlysis.moodbe.integration.Form;
import com.moodlysis.moodbe.integration.Series;

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
    
    public String getJSON(Form.formInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		output.put("formID", info.formID);
		output.put("hostID", info.hostID);
		data.put("title", info.title);
		data.put("description", info.description);
		output.put("data", data);
		return output.toJSONString();
	}
    
    public String getJSON(int[] formIDs) {
    	JSONObject output = new JSONObject();
    	JSONArray forms = new JSONArray();
    	for (int i = 0; i < formIDs.length; i++) {
    		forms.add(formIDs[i]);
    	}
    	output.put("formID", forms);
    	return output.toJSONString();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if (request.getRequestURI().matches("/v0/forms/(.*)")) {
			doGetForm(request, response);
		}
		else if (request.getRequestURI().equals("/v0/forms")) {
			doGetHostForms(request, response);
		}
	}
	
	protected void doGetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//TODO get the question ids for the form
		Form form = new Form(response.getWriter());
		int formID = GeneralRequest.getIDFromPath(request, response);
		
		Form.formInfo info = form.getForm(formID);
		int hostID = info.hostID;
		String title = info.title;
		String description = info.description;
		
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
		Form form = new Form(response.getWriter());
		int hostID = GeneralRequest.getIDFromQuery(request, response);
		
		int[] formIDs = form.getFormIDsForHost(hostID);
		
		
		// tell the caller that this is JSON content (move to front)
		if (formIDs == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
		Form form = new Form(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int hostID = -1;
		String data = "";
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
			data = postObject.get("data").toString();
			title = ((JSONObject) postObject.get("data")).get("title").toString();
			description = ((JSONObject) postObject.get("data")).get("description").toString();
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
		}
		//CALL JDBC
		int formID = form.newForm(hostID, title, description);
		
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
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
