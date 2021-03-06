package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.GeneralRequest;
import com.moodlysis.moodbe.integrationinterfaces.FormInterface;

public class Form implements FormInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Form(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	public static class formInfo {
		public int formID;
		public int hostID;
		public String title;
		public String description;
		public int[] questionIDs;
	}
	
	
	

	@Override
	public int[] getFormIDsForHost(int hostID) {
		PreparedStatement formsGet  = null;
		ResultSet table = null;
		int maxForms = 100;
		int[] formIDs = new int[maxForms];
		int i = 0;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM FORMS WHERE HostID = ?";
    		formsGet = conn.prepareStatement(query);
    		formsGet.setInt(1, hostID);
			table = formsGet.executeQuery();
			while(table.next()) {
				formIDs[i] = table.getInt("FormID");
				i++;
				if (i == maxForms) {
					maxForms = maxForms * 2;
					formIDs = GeneralRequest.extendArray(formIDs,maxForms);
				}
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
		} finally {
			try {
				if (formsGet != null) {
					formsGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		int[] returnIDs = new int[i];
		for (int j = 0; j < i; j++) {
			returnIDs[j] = formIDs[j];
		}
		return returnIDs;
	}

	public formInfo getForm(int formID) {
		formInfo info = new formInfo();
		info.formID = formID;
		PreparedStatement formGet  = null;
		PreparedStatement questionsGet = null;
		ResultSet table = null;
		ResultSet table2 = null;
		int hostID = -1;
		String title = "";
		String desc = "";
		int maxQuestions = 30;
		int i = 0;
		int[] questionIDs = new int[maxQuestions];
		
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM FORMS WHERE FormID = ?";
    		formGet = conn.prepareStatement(query);
    		formGet.setInt(1, formID);
			table = formGet.executeQuery();
			table.next();
			hostID = table.getInt("HostID");
			title = table.getString("Title");
			desc = table.getString("Description");
			if (table.wasNull()) {
				desc = "";
			}
			String query2 = "SELECT * FROM QUESTIONS WHERE FormID = ?";
			questionsGet = conn.prepareStatement(query2);
			questionsGet.setInt(1, formID);
			table2 = questionsGet.executeQuery();
			while (table2.next()) {
				questionIDs[i] = table2.getInt("QuestionID");
				i++;
				if (i == maxQuestions) {
					maxQuestions = maxQuestions * 2;
					questionIDs = GeneralRequest.extendArray(questionIDs, maxQuestions);
				}
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
		} finally {
			try {
				if (formGet != null) {
					formGet.close();
				}
				if (table != null) {
					table.close();
				}
				if (questionsGet != null) {
					questionsGet.close();
				}
				if (table2 != null) {
					table2.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		int[] returnIDs = new int[i];
		for (int j = 0; j < i; j++) {
			returnIDs[j] = questionIDs[j];
		}
		info.hostID = hostID;
		info.title = title;
		info.description = desc;
		info.questionIDs = returnIDs;
		return info;
	}

	@Override
	public int newForm(int hostID, String title, String desc) {
		// TODO Auto-generated method stub
		PreparedStatement formInsert  = null;
		ResultSet formKey = null;
		int formID = -1;
		try {
			conn.setAutoCommit(false);
    		String query = "INSERT INTO FORMS VALUES (nextval('FormsFormID'),?,?,?)";
    		formInsert = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		formInsert.setInt(1, hostID);
    		formInsert.setString(2, title);
    		formInsert.setString(3, desc);
    		formInsert.executeUpdate();
    		formKey = formInsert.getGeneratedKeys();
    		formKey.next();
    		formID = formKey.getInt(1);
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
				if (formInsert != null) {
					formInsert.close();
				}
				if (formKey != null) {
					formKey.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		return formID;
	}

	@Override
	public boolean editForm(int formID, String newTitle, String newDesc) {
		// TODO Auto-generated method stub
		PreparedStatement formEdit  = null;
		try {
			conn.setAutoCommit(false);
    		String query = "UPDATE FORMS SET Title = ?, Description = ? WHERE FormID = ?";
    		formEdit = conn.prepareStatement(query);
    		formEdit.setString(1, newTitle);
    		formEdit.setString(2, newDesc);
    		formEdit.setInt(3, formID);
    		formEdit.executeUpdate();
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
				if (formEdit != null) {
					formEdit.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean deleteForm(int formID) {
		// TODO Auto-generated method stub
		PreparedStatement formDelete  = null;
		try {
			conn.setAutoCommit(false);
    		String query = "DELETE FROM FORMS WHERE FormID = ?";
    		formDelete = conn.prepareStatement(query);
    		formDelete.setInt(1, formID);
    		formDelete.executeUpdate();
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
				if (formDelete != null) {
					formDelete.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

}
