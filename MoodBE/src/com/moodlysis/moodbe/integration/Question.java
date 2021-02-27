package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integration.Series.seriesInfo;
import com.moodlysis.moodbe.integrationinterfaces.QuestionInterface;

public class Question implements QuestionInterface {

	private Connection conn;
	private PrintWriter writer;
	
	public Question(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	public static class questionInfo {
		public int questionID;
		public int formID;
		public String type;
		public String text;
		public String options;
		public int numInForm;
	}
	
	public questionInfo getQuestion(int questionID) {
		questionInfo info = new questionInfo();
		info.questionID = questionID;
		PreparedStatement questionGet  = null;
		ResultSet table = null;
		int formID = -1;
		int numInForm = -1;
		String type = null;
		String text = null;
		String options = null;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM QUESTIONS WHERE QuestionID = ?";
    		questionGet = conn.prepareStatement(query);
    		questionGet.setInt(1, questionID);
			table = questionGet.executeQuery();
			table.next();
			formID = table.getInt("FormID");
			numInForm = table.getInt("NumInForm");
			type = table.getString("Type");
			text = table.getString("Content");
			options = table.getString("Options");
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			//TODO
			e.printStackTrace(this.writer);
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
		} finally {
			try {
				if (questionGet != null) {
					questionGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		info.formID = formID;
		info.numInForm = numInForm;
		info.type = type;
		info.text = text;
		info.options = options;
		return info;
	}

	@Override
	public int newQuestion(int formID, String questionType, String text, String options) {
		PreparedStatement numInFormGet  = null;
		ResultSet table = null;
		PreparedStatement questionInsert  = null;
		ResultSet questionKey = null;
		int questionID = -1;
		int numInForm = -1;
		try {
			conn.setAutoCommit(false);
			String queryGetNumInForm = "SELECT * FROM QUESTIONS WHERE FormID = ? ORDER BY NumInForm DESC";
			numInFormGet = conn.prepareStatement(queryGetNumInForm);
			numInFormGet.setInt(1, formID);
			table = numInFormGet.executeQuery();
			if (table.next()) {
				numInForm = table.getInt("NumInForm") + 1;
			}
			else {
				numInForm = 1;
			}
			String queryInsert = "INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'),?,?,?,?,?)";
    		questionInsert = conn.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);
    		questionInsert.setString(1, questionType);
    		questionInsert.setInt(2, formID);
    		questionInsert.setInt(3, numInForm);
    		questionInsert.setString(4, text);
    		questionInsert.setString(5, options);
    		questionInsert.executeUpdate();
    		questionKey = questionInsert.getGeneratedKeys();
    		questionKey.next();
    		questionID = questionKey.getInt(1);
			conn.commit();
			conn.setAutoCommit(true);
		} catch(SQLException e) {
			//TODO
			e.printStackTrace(this.writer);
			try {
				conn.rollback();
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
		} finally {
			try {
				if (questionInsert != null) {
					questionInsert.close();
				}
				if (questionKey != null) {
					questionKey.close();
				}
				if (numInFormGet != null) {
					numInFormGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		return questionID;
	}

	@Override
	public boolean editQuestion(int questionID, String questionType, int numInForm, String text, String options) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteQuestion(int questionID) {
		// TODO Auto-generated method stub
		return false;
	}

}
