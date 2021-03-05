package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
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
	public boolean editQuestionDetails(int questionID, String questionType, String text, String options) {
		// TODO fix so if error occurs return false but still execute finally statement
		PreparedStatement questionEdit  = null;
		try {
			conn.setAutoCommit(false);
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
				return false;
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
				return false;
			}
		} finally {
			try {
				if (questionEdit != null) {
					questionEdit.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}
	
	public boolean editQuestionPosition(int questionID, int previousID) {
		PreparedStatement questionEdit  = null;
		PreparedStatement getNumInForm = null;
		ResultSet table = null;
		int numInForm = -1;
		try {
			conn.setAutoCommit(false);
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
				return false;
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
				return false;
			}
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
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean deleteQuestion(int questionID) {
		PreparedStatement questionDelete  = null;
		try {
			conn.setAutoCommit(false);
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
				return false;
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
				return false;
			}
		} finally {
			try {
				if (questionDelete != null) {
					questionDelete.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

}
