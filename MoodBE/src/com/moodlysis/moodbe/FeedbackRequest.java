package com.moodlysis.moodbe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.moodlysis.moodbe.integration.Answer;
import com.moodlysis.moodbe.integration.Feedback;
import com.moodlysis.moodbe.requestexceptions.*;



@WebServlet({"/v0/feedback", "/v0/feedback/*"})
public class FeedbackRequest extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackRequest() {
        super();
    }
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String eventIDString = request.getParameter("eventID");
		String attendeeIDString = request.getParameter("attendeeID");
		String timeUpdatedSinceString = request.getParameter("time-updated-since");
		
		int eventID;
		int attendeeID;
		
		// must include eventID
		if (eventIDString == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "must include ?eventID=...");
			return;
		}
		
		// parse eventID
		try {
			eventID = Integer.parseInt(eventIDString);
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "unable to parse attendeeID: " + e.toString());
			return;
		}
		
		// TODO - uncomment when getHostIDFromAuthToken is implemented
		int hostIDAuth = 0;
//		int hostIDAuth = GeneralRequest.getHostIDFromAuthToken(request);
		
		if (hostIDAuth == -1) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in as a host");
			return;
		}
		
		

		Feedback.FeedbackInfo feedbackInfo = null;
		Feedback.FeedbackAttendeeInfo feedbackAttendeeInfo = null;
		
		// GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}
		if (attendeeIDString == null && timeUpdatedSinceString != null) {
			
			LocalDateTime timeUpdatedSince;

			try {
				timeUpdatedSince = LocalDateTime.parse(timeUpdatedSinceString);
			} catch (DateTimeParseException e) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid timestamp format. Do something like this: 2007-12-03T10:15:30, or anything else that can be parsed by LocalDateTime.parse");
				return;
			}
			
			Connection conn = DatabaseConnection.getConnection();
			Feedback feedback = new Feedback(response.getWriter(), conn);
			try {
				feedbackInfo = feedback.getFeedbackSince(eventID, timeUpdatedSince, hostIDAuth);
			} catch (MoodlysisInternalServerError e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error: " + e.toString());
				e.printStackTrace();
				return;
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		} else if (attendeeIDString == null && timeUpdatedSinceString == null) {
			Connection conn = DatabaseConnection.getConnection();
			Feedback feedback = new Feedback(response.getWriter(), conn);
			
			try {
				feedbackInfo = feedback.getAllFeedback(eventID, hostIDAuth);
			} catch (MoodlysisInternalServerError e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error: " + e.toString());
				e.printStackTrace();
				return;
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} else if (attendeeIDString != null && timeUpdatedSinceString == null) {
			Connection conn = DatabaseConnection.getConnection();
			Feedback feedback = new Feedback(response.getWriter(), conn);
			
			try {
				try {
					attendeeID = Integer.parseInt(attendeeIDString);
				} catch (NumberFormatException e) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "unable to parse attendeeID: " + e.toString());
					return;
				}
				feedbackAttendeeInfo = feedback.getAttendeeFeedback(eventID, attendeeID);
			} catch (MoodlysisInternalServerError e) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "database error: " + e.toString());
				e.printStackTrace();
				return;
			} catch (MoodlysisNotFound e) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "database error: " + e.toString());
				e.printStackTrace();
				return;
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} else {
			// TODO add other situations of attendeeIDString and timeUpdatedSinceString
			response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
			return;
		}
		String responseJSON = "";
		if (feedbackInfo != null) {
			responseJSON = getJSON(feedbackInfo);
		}
		if (feedbackAttendeeInfo != null) {
			responseJSON = getJSON(feedbackAttendeeInfo);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(responseJSON);

		
	}


	@SuppressWarnings("unchecked")
	private String getJSON(Feedback.FeedbackInfo info) {
		
		JSONObject output = new JSONObject();
		output.put("eventID", info.eventID);
		if (info.timeUpdatedSince != null)
			output.put("time-updated-since", info.timeUpdatedSince.toString());
		else
			output.put("time-updated-since", null);
		output.put("contains", info.list.size());
		
		JSONArray list = new JSONArray();
		for (Feedback.SubmissionInfo subInfo : info.list) {
			JSONObject subObj = new JSONObject();
			subObj.put("formID", subInfo.formID);
			subObj.put("eventFormID", subInfo.eventFormID);
			subObj.put("account-name", subInfo.accountName);
			subObj.put("attendeeID", subInfo.attendeeID);
			subObj.put("time-updated", subInfo.timeUpdated.toString());
			subObj.put("is-edited", subInfo.isEdited);
			
			JSONArray answers = new JSONArray();
			for (Answer.AnswerInfo ansInfo : subInfo.answers) {
				JSONObject ansObj = new JSONObject();
				ansObj.put("questionID", ansInfo.questionID);
				ansObj.put("answerID", ansInfo.answerID);
				if (ansInfo.moodValue != null)
					ansObj.put("mood-value", ansInfo.moodValue);
				ansObj.put("is-edited", ansInfo.isEdited);
				ansObj.put("time-updated", ansInfo.timeSubmitted.toString());
				JSONObject data = new JSONObject();
				data.put("response", ansInfo.response);
				ansObj.put("data", data);
				answers.add(ansObj);
			}
			subObj.put("answers", answers);
			list.add(subObj);
		}
		output.put("list", list);
		return output.toJSONString();
	}
	
	@SuppressWarnings("unchecked")
	private String getJSON(Feedback.FeedbackAttendeeInfo info) {
		
		JSONObject output = new JSONObject();
		output.put("eventID", info.eventID);
		output.put("contains", info.list.size());
		
		JSONArray list = new JSONArray();
		for (Feedback.SubmissionInfo subInfo : info.list) {
			JSONObject subObj = new JSONObject();
			subObj.put("formID", subInfo.formID);
			subObj.put("eventFormID", subInfo.eventFormID);
			subObj.put("account-name", subInfo.accountName);
			subObj.put("attendeeID", subInfo.attendeeID);
			subObj.put("time-updated", subInfo.timeUpdated.toString());
			subObj.put("is-edited", subInfo.isEdited);
			
			JSONArray answers = new JSONArray();
			for (Answer.AnswerInfo ansInfo : subInfo.answers) {
				JSONObject ansObj = new JSONObject();
				ansObj.put("questionID", ansInfo.questionID);
				ansObj.put("answerID", ansInfo.answerID);
				ansObj.put("mood-value", ansInfo.moodValue.toString());
				ansObj.put("is-edited", ansInfo.isEdited);
				ansObj.put("time-updated", ansInfo.timeSubmitted.toString());
				JSONObject data = new JSONObject();
				data.put("response", ansInfo.response);
				ansObj.put("data", data);
				answers.add(ansObj);
			}
			subObj.put("answers", answers);
			list.add(subObj);
		}
		output.put("list", list);
		return output.toJSONString();
	}

	

}
