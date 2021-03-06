package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.HostInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class Host implements HostInterface {
	
	private Connection conn;
	private PrintWriter writer;
	
	public Host(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	@Override
	public hostInfo getHost(int hostID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		hostInfo info = new hostInfo();
		info.hostID = hostID;
		PreparedStatement hostGet  = null;
		ResultSet table = null;
		String email = "";
		String pass = "";
		String account = "";
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM HOST WHERE HostID = ?";
    		hostGet = conn.prepareStatement(query);
    		hostGet.setInt(1, hostID);
			table = hostGet.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Host not found. Host account may have expired or been deleted.");
			}
			email = table.getString("Email");
			pass = table.getString("Pass");
			account = table.getString("AccountName");
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
				if (hostGet != null) {
					hostGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		info.email = email;
		info.pass = pass;
		info.account = account;
		return info;
	}
	

	@Override
	public int newHost(String email, String pass, String account) throws MoodlysisInternalServerError {
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
			throw new MoodlysisInternalServerError(e.toString());
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
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return hostID;
	}

	@Override
	public boolean editHost(int hostID, String email, String pass, String account) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO Auto-generated method stub
		PreparedStatement hostEdit  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM HOST WHERE HostID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, hostID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Host not found. Host account may have expired or been deleted.");
			}
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
			} catch(SQLException er) {
				er.printStackTrace(this.writer);
			}
			throw new MoodlysisInternalServerError(e.toString());
		} finally {
			try {
				if (hostEdit != null) {
					hostEdit.close();
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
	public boolean deleteHost(int hostID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		PreparedStatement hostDelete  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM HOST WHERE HostID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, hostID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("Host not found. Host account may have expired or been deleted.");
			}
    		String query = "DELETE FROM HOST WHERE HostID = ?";
    		hostDelete = conn.prepareStatement(query);
    		hostDelete.setInt(1, hostID);
    		hostDelete.executeUpdate();
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
				if (hostDelete != null) {
					hostDelete.close();
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
