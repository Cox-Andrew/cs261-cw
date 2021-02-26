package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    		String query = "SELECT * FROM FORMS WHERE hostID = ?";
    		formsGet = conn.prepareStatement(query);
    		formsGet.setInt(1, hostID);
			table = formsGet.executeQuery();
			while(table.next()) {
				formIDs[i] = table.getInt("formID");
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
		ResultSet table = null;
		int hostID = -1;
		String title = "";
		String desc = "";
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM FORMS WHERE FormID = ?";
    		formGet = conn.prepareStatement(query);
    		formGet.setInt(1, formID);
			table = formGet.executeQuery();
			table.next();
			hostID = table.getInt("hostID");
			title = table.getString("Title");
			desc = table.getString("Description");
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
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		info.hostID = hostID;
		info.title = title;
		info.description = desc;
		return info;
	}

	@Override
	public int newForm(int hostID, String formTitle, String formDescription) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean editForm(int formID, String formTitle, String formDescription) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteForm(int formID) {
		// TODO Auto-generated method stub
		return false;
	}

}
