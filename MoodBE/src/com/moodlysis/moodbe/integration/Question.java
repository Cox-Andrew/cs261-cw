package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.integrationinterfaces.QuestionInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class Question implements QuestionInterface {

	private Connection conn;
	private PrintWriter writer;
	
	public Question(PrintWriter writer, Connection conn) {
		this.conn = conn;
		this.writer = writer;
	}
	
	@Override
	public questionInfo getQuestion(int questionID) throws MoodlysisInternalServerError, MoodlysisNotFound {
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
			if (!table.next()) {
				throw new MoodlysisNotFound("Question not found. Question may have expired or been deleted.");
			}
			formID = table.getInt("FormID");
			numInForm = table.getInt("NumInForm");
			type = table.getString("Type");
			text = table.getString("Content");
			if (table.wasNull()) {
				text = "";
			}
			options = table.getString("Options");
			if (table.wasNull()) {
				options = "";
			}
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
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
	public int newQuestion(int formID, int previousID, String questionType, String text, String options) throws MoodlysisInternalServerError, MoodlysisNotFound {
		PreparedStatement numInFormGet  = null;
		ResultSet table = null;
		PreparedStatement questionInsert  = null;
		ResultSet questionKey = null;
		int questionID = -1;
		int numInForm = -1;
		try {
			conn.setAutoCommit(false);
			if (previousID == 0) {
				numInForm = 1;
			}
			else {
				String queryGetNumInForm = "SELECT * FROM QUESTIONS WHERE QuestionID = ? AND FormID = ? ORDER BY NumInForm DESC";
				numInFormGet = conn.prepareStatement(queryGetNumInForm);
				numInFormGet.setInt(1, previousID);
				numInFormGet.setInt(2, formID);
				table = numInFormGet.executeQuery();
				if (table.next()) {
					numInForm = table.getInt("NumInForm") + 1;
				}
				else {
					throw new MoodlysisNotFound("Previous question not found. Question may have expired, been deleted or is part of a different form");
				}
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return questionID;
	}

	@Override
	public boolean editQuestionDetails(int questionID, String questionType, String text, String options) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO fix so if error occurs return false but still execute finally statement
		PreparedStatement questionEdit  = null;PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM QUESTIONS WHERE QuestionID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, questionID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Question not found. Question may have expired or been deleted.");
			}
    		String query = "UPDATE QUESTIONS SET Type = ?, Content = ?, Options = ? WHERE QuestionID = ?";
    		questionEdit = conn.prepareStatement(query);
    		questionEdit.setString(1, questionType);
    		questionEdit.setString(2, text);
    		questionEdit.setString(3, options);
    		questionEdit.setInt(4, questionID);
    		questionEdit.executeUpdate();
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
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (questionEdit != null) {
					questionEdit.close();
				}
				if (existCheck != null) {
					existCheck.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return true;
	}
	
	@Override
	public boolean editQuestionPosition(int questionID, int previousID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		PreparedStatement questionEdit  = null;
		PreparedStatement getNumInForm = null;
		ResultSet table = null;
		PreparedStatement existCheck = null;
		ResultSet table2 = null;
		int numInForm = -1;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM QUESTIONS WHERE QuestionID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, questionID);
			table2 = existCheck.executeQuery();
			if (!table2.next()) {
				throw new MoodlysisNotFound("Question not found. Question may have expired or been deleted.");
			}
			if (previousID > 0) {
				//Query makes sure forms of previous id and given question id are the same
				//e.g. SELECT QUESTIONS.QuestionID, QUESTIONS.NumInForm FROM (SELECT * FROM QUESTIONS WHERE QuestionID = 4) AS QUESTION INNER JOIN QUESTIONS ON (QUESTION.FormID = QUESTIONS.FormID) WHERE QUESTIONS.QuestionID = 5 OR QUESTIONS.QuestionID = 4 ORDER BY QUESTIONS.NumInForm DESC;
				String queryGetNumInForm = "SELECT QUESTIONS.QuestionID, QUESTIONS.NumInForm "
										 + "FROM (SELECT * FROM QUESTIONS WHERE QuestionID = ?) AS QUESTION "
										 + "INNER JOIN QUESTIONS ON (QUESTION.FormID = QUESTIONS.FormID) "
										 + "WHERE QUESTIONS.QuestionID = ? OR QUESTIONS.QuestionID = ? "
										 + "ORDER BY QUESTIONS.NumInForm DESC";
				getNumInForm = conn.prepareStatement(queryGetNumInForm);
				getNumInForm.setInt(1, questionID);
				getNumInForm.setInt(2, previousID);
				getNumInForm.setInt(3, questionID);
				table = getNumInForm.executeQuery();
				table.next();
				if (table.getInt("QuestionID") == questionID) {
					if (table.next()) {
						//perhaps do check form number is from the previousID
						numInForm = table.getInt("NumInForm") + 1;
					}
					else {
						//TODO scenario where forms do not match or questionID is set to be after itself
						return false;
					}
				}
				else if (table.getInt("QuestionID") == previousID) {
					numInForm = table.getInt("NumInForm");
				}
			}
			else if (previousID == 0) {
				numInForm = 1;
			}
    		String queryUpdate = "UPDATE QUESTIONS SET NumInForm = ? WHERE QuestionID = ?";
    		questionEdit = conn.prepareStatement(queryUpdate);
    		questionEdit.setInt(1, numInForm);
    		questionEdit.setInt(2, questionID);
    		questionEdit.executeUpdate();
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
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (questionEdit != null) {
					questionEdit.close();
				}
				if (getNumInForm != null) {
					getNumInForm.close();
				}
				if (table != null) {
					table.close();
				}
				if (existCheck != null) {
					existCheck.close();
				}
				if (table2 != null) {
					table2.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return true;
	}

	@Override
	public boolean deleteQuestion(int questionID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		PreparedStatement questionDelete  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM QUESTIONS WHERE QuestionID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, questionID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Question not found. Question may have expired or been deleted.");
			}
    		String query = "DELETE FROM QUESTIONS WHERE QuestionID = ?";
    		questionDelete = conn.prepareStatement(query);
    		questionDelete.setInt(1, questionID);
    		questionDelete.executeUpdate();
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
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (questionDelete != null) {
					questionDelete.close();
				}
				if (existCheck != null) {
					existCheck.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return true;
	}

}
