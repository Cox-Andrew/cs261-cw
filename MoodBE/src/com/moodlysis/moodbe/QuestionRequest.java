package com.moodlysis.moodbe;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.moodlysis.moodbe.integration.Question;
import com.moodlysis.moodbe.integration.Series;


/**
 * Servlet implementation class QuestionRequest
 */
@WebServlet(urlPatterns = {"/v0/questions", "/v0/questions/*"})
public class QuestionRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuestionRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public String getJSON(int questionID) {
		// TODO Auto-generated method stub
		/*
		 * JSON Example Output
		 * { "questionID": 1 }
		 */
		
		JSONObject output = new JSONObject();
		output.put("questionID", questionID);
		return output.toJSONString();
	}
	
	public String getJSON(Question.questionInfo info) {
		JSONObject output = new JSONObject();
		JSONObject data = new JSONObject();
		JSONArray options = new JSONArray();
		JSONParser parser = new JSONParser();
		try {
			options = (JSONArray) parser.parse(info.options);
		} catch (ParseException e) {
			// TODO Generate correct error
			e.printStackTrace();
		}
		output.put("questionID", info.questionID);
		output.put("formID", info.formID);
		data.put("type", info.type);
		data.put("text", info.text);
		data.put("options", options);
		output.put("data", data);
		return output.toJSONString();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//if  /v0/series/{seriesID}
		doGetQuestion(request, response);
	}
	
	protected void doGetQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Question question = new Question(response.getWriter());
		int questionID = GeneralRequest.getIDFromPath(request, response);
		
		Question.questionInfo info = question.getQuestion(questionID);
		int formID = info.formID;
		String type = info.type;
		String text = info.text;
		String options = info.options;
		
		// tell the caller that this is JSON content (move to front)
		// make a proper check for text/options which isnt just empty string
		if (formID == -1 || type.equals("") /*|| text.equals("") || options.equals("")*/) {
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
		if (request.getRequestURI().equals("/v0/questions")) {
			doNewQuestion(request, response);
		}
		else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	protected void doNewQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Question question = new Question(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser postParser = new JSONParser();
		int formID = -1;
		String data = "";
		String type = null;
		String text = null;
		String options = null;
		try {
			JSONObject postObject = (JSONObject) postParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"formID": 1,
			 *		"data": {
			 *			"type": "multi",
			 *			"text": "Choose one of the options",
			 *			"options": [
			 *				"Option 1",
			 *				"Option 2",
			 *				"Option 3"
			 *			]
			 *		}
			 *	}
 			 *
			 *
			 * {"formID": 1, "data": {"type": "multi","text": "Choose one of the options","options": ["Option 1","Option 2","Option 3"]}}
			 * 
			 * JSON looks like the above
			 */
			formID = Integer.valueOf(postObject.get("formID").toString());
			data = postObject.get("data").toString();
			type = ((JSONObject) postObject.get("data")).get("type").toString();
			text = ((JSONObject) postObject.get("data")).get("text").toString();
			options = ((JSONObject) postObject.get("data")).get("options").toString();
			response.getWriter().append(options);
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
		}
		//CALL JDBC
		int questionID = question.newQuestion(formID, type, text, options);
		
		// tell the caller that this is JSON content (move to front)
		if (questionID != -1) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			
			//Write JSON
			String output = getJSON(questionID);
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
		//if /v0/questions/{questionID}
		doEditQuestion(request, response);
		
	}
	
	protected void doEditQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Question question = new Question(response.getWriter());
		String jsonData = GeneralRequest.readJSON(request, response);
		JSONParser putParser = new JSONParser();
		int questionID = GeneralRequest.getIDFromPath(request, response);
		String data = "";
		String type = null;
		String text = null;
		String options = null;
		int previousID = -1;
		try {
			JSONObject putObject = (JSONObject) putParser.parse(jsonData); //can directly use reader rather than string
			/*
			 * {
			 *		"data": {
			 *			"type": "rating",
			 *			"text": "Choose one of the options",
			 *			"options": null
			 *		}
			 *	}
 			 *
			 *
			 * {"data": {"type": "multi","text": "Choose one of the options","options": null}}
			 * 
			 * 
			 * {
			 *     "preceding-questionID": 2 
			 * }
			 * 
			 * {"preceding-questionID": 2}
			 * 
			 * JSON looks like either of the above
			 */
			if (putObject.get("data") != null) {
				data = putObject.get("data").toString();
				type = "";
				text = "";
				options = "";
				if (((JSONObject) putObject.get("data")).get("type") != null) {
					type = ((JSONObject) putObject.get("data")).get("type").toString();
				}
				if (((JSONObject) putObject.get("data")).get("text") != null) {
					text = ((JSONObject) putObject.get("data")).get("text").toString();
				}
				if (((JSONObject) putObject.get("data")).get("options") != null) {
					options = ((JSONObject) putObject.get("data")).get("options").toString();
				}
			}
			else if (putObject.get("preceding-questionID") != null) {
				previousID = Integer.valueOf(putObject.get("preceding-questionID").toString());
			}
		} catch(ParseException e) {
			//TODO
			response.getWriter().append(jsonData + "\n\n\n");
			e.printStackTrace(response.getWriter());
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//return;
		}
		//TODO check if edit question content or edit position
		if (previousID != -1) {
			if (question.editQuestionPosition(questionID, previousID)) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				//assumes id not found
				//put into response body what was wrong (can be handled in jdbc)
			}
		}
		else /*if type not null e.g.*/ {
			if (question.editQuestionDetails(questionID, type, text, options)) {
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				//assumes id not found
				//put into response body what was wrong (can be handled in jdbc)
			}
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
