package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.FormInterface;

public class Form implements FormInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Form(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}

	@Override
	public int[] getFormIDsForHost(int hostID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getForm(int formID) {
		// TODO Auto-generated method stub
		return null;
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
