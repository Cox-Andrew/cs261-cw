package com.moodlysis.moodbe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.moodlysis.moodbe.integration.Form;

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
		output.put("formID", info.formID);
		output.put("hostID", info.hostID);
		output.put("title", info.title);
		output.put("description", info.description);
		return output.toJSONString();
	}
    
    public String getJSON(int[] formIDs) {
    	JSONObject output = new JSONObject();
    	for (int i = 0; i < formIDs.length; i++) {
    		output.put("formID", formIDs[i]);
		}
    	return output.toJSONString();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if  /v0/forms/{formID}
		doGetForm(request, response);
		//if /v0/forms?hostID={hostID}
		//doGetHostForms
	}
	
	protected void doGetForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
