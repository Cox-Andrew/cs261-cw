package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.HostInterface;

public class Host implements HostInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Host(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	

	@Override
	public int newHost(String email, String pass, String account) {
		PreparedStatement hostInsert  = null;
		ResultSet hostKey = null;
		int hostID = -1;
		try {
			conn.setAutoCommit(false);
    		String query = "INSERT INTO HOST VALUES (nextval('HostsHostID'),?,?,?)";
    		hostInsert = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		hostInsert.setString(1, email);
    		hostInsert.setString(2, pass);
    		hostInsert.setString(3, account);
    		hostInsert.executeUpdate();
    		hostKey = hostInsert.getGeneratedKeys();
    		hostKey.next();
			hostID = hostKey.getInt(1);
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
				if (hostInsert != null) {
					hostInsert.close();
				}
				if (hostKey != null) {
					hostKey.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
			}
		}
		return hostID;
	}

	@Override
	public boolean editHost(int hostID, String email, String pass, String account) {
		// TODO Auto-generated method stub
		PreparedStatement hostEdit  = null;
		try {
			conn.setAutoCommit(false);
    		String query = "UPDATE HOST SET Email = ?, Pass = ?, AccountName = ? WHERE HostID = ?";
    		hostEdit = conn.prepareStatement(query);
    		hostEdit.setString(1, email);
    		hostEdit.setString(2, pass);
    		hostEdit.setString(3, account);
    		hostEdit.setInt(4, hostID);
    		hostEdit.executeUpdate();
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
				if (hostEdit != null) {
					hostEdit.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				return false;
			}
		}
		return true;
	}

}
