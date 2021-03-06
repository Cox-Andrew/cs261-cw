package com.moodlysis.moodbe.integration;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.moodlysis.moodbe.DatabaseConnection;
import com.moodlysis.moodbe.integrationinterfaces.EventFormInterface;
import com.moodlysis.moodbe.requestexceptions.MoodlysisInternalServerError;
import com.moodlysis.moodbe.requestexceptions.MoodlysisNotFound;

public class EventForm implements EventFormInterface {

	
	private Connection conn;
	private PrintWriter writer;
	
	public EventForm(PrintWriter writer) {
		this.conn = DatabaseConnection.getConnection();
		this.writer = writer;
	}
	
	@Override
	public eventFormInfo getEventForm(int eventFormID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		eventFormInfo info = new eventFormInfo();
		info.eventFormID = eventFormID;
		PreparedStatement eventFormGet  = null;
		ResultSet table = null;
		int eventID = -1;
		int formID = -1;
		int numInEvent = -1;
		Boolean isActive = false;
		try {
			conn.setAutoCommit(false);
    		String query = "SELECT * FROM EVENTFORMS WHERE EventFormID = ?";
    		eventFormGet = conn.prepareStatement(query);
    		eventFormGet.setInt(1, eventFormID);
			table = eventFormGet.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("EventForm not found. EventForm may have expired or been deleted.");
			}
			eventID = table.getInt("EventID");
			formID = table.getInt("FormID");
			numInEvent = table.getInt("NumInEvent");
			isActive = table.getBoolean("IsActive");
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
				if (eventFormGet != null) {
					eventFormGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		info.eventID = eventID;
		info.formID = formID;
		info.numInEvent = numInEvent;
		info.isActive = isActive;
		return info;
	}

	@Override
	public int newEventForm(int eventID, int formID, Boolean isActive) throws MoodlysisInternalServerError {
		// TODO Auto-generated method stub
		//JDBC

		PreparedStatement eventFormInsert  = null;
		PreparedStatement numInEventGet  = null;
		ResultSet table = null;
		ResultSet eventFormKey = null;
		int eventFormID = -1;
		int numInEvent = -1;
		try {
			conn.setAutoCommit(false);
			String queryGetNumInEvent = "SELECT * FROM EVENTFORMS WHERE EventID = ? ORDER BY NumInEvent DESC";
			numInEventGet = conn.prepareStatement(queryGetNumInEvent);
			numInEventGet.setInt(1, eventID);
			table = numInEventGet.executeQuery();
			if (table.next()) {
				numInEvent = table.getInt("NumInEvent") + 1;
			}
			else {
				numInEvent = 1;
			}
    		String query = "INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'),?,?,?,?)";
    		eventFormInsert = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
    		eventFormInsert.setInt(1, eventID);
    		eventFormInsert.setInt(2, formID);
    		eventFormInsert.setInt(3, numInEvent);
    		eventFormInsert.setBoolean(4, isActive);
    		eventFormInsert.executeUpdate();
    		eventFormKey = eventFormInsert.getGeneratedKeys();
    		eventFormKey.next();
    		eventFormID = eventFormKey.getInt(1);
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
				if (eventFormInsert != null) {
					eventFormInsert.close();
				}
				if (eventFormKey != null) {
					eventFormKey.close();
				}
				if (numInEventGet != null) {
					numInEventGet.close();
				}
				if (table != null) {
					table.close();
				}
			} catch(SQLException e) {
				e.printStackTrace(this.writer);
				throw new MoodlysisInternalServerError(e.toString());
			}
		}
		return eventFormID;
	}

	@Override
	public boolean editEventForm(int eventFormID, int previousID ,Boolean isActive) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO fix so if error occurs return false but still execute finally statement
		// previousID -2 to continue without changing the position
		PreparedStatement eventFormEdit  = null;
		PreparedStatement getNumInEvent = null;
		ResultSet table = null;
		PreparedStatement existCheck = null;
		ResultSet table2 = null;
		int numInEvent = -1;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM EVENTFORMS WHERE EventFormID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, eventFormID);
			table2 = existCheck.executeQuery();
			if (!table2.next()) {
				throw new MoodlysisNotFound("EventForm not found. EventForm may have expired or been deleted.");
			}
			if (previousID > 0) {
				//Query makes sure forms of previous id and given question id are the same
				//e.g. SELECT EVENTFORMS.EventFormID, EVENTFORMS.NumInEvent FROM (SELECT * FROM EVENTFORMS WHERE EventFormID = 4) AS EVENTFORM INNER JOIN EVENTFORMS ON (EVENTFORM.EventID = EVENTFORMS.EventID) WHERE EVENTFORMS.EventFormID = 5 OR EVENTFORMS.EventFormID = 4 ORDER BY EVENTFORMS.NumInEvent DESC;
				String queryGetNumInEvent = "SELECT EVENTFORMS.EventFormID, EVENTFORMS.NumInEvent "
										  + "FROM (SELECT * FROM EVENTFORMS WHERE EventFormID = ?) AS EVENTFORM "
										  + "INNER JOIN EVENTFORMS ON (EVENTFORM.EventID = EVENTFORMS.EventID) "
										  + "WHERE EVENTFORMS.EventFormID = 5 OR EVENTFORMS.EventFormID = 4 "
										  + "ORDER BY EVENTFORMS.NumInEvent DESC;";
				getNumInEvent = conn.prepareStatement(queryGetNumInEvent);
				getNumInEvent.setInt(1, eventFormID);
				getNumInEvent.setInt(2, previousID);
				getNumInEvent.setInt(3, eventFormID);
				table = getNumInEvent.executeQuery();
				table.next();
				if (table.getInt("EventFormID") == eventFormID) {
					if (table.next()) {
						//perhaps do check form number is from the previousID
						numInEvent = table.getInt("NumInEvent") + 1;
					}
					else {
						//TODO scenario where forms do not match or eventFormID is set to be after itself
						return false;
					}
				}
				else if (table.getInt("EventFormID") == previousID) {
					numInEvent = table.getInt("NumInEvent");
				}
			}
			else if (previousID == 0) {
				numInEvent = 1;
			}
			if (previousID == -2) {
				//TODO don't change NumInEvent 
				String queryUpdate = "UPDATE EVENTFORMS SET IsActive = ? WHERE EventFormID = ?";
				eventFormEdit = conn.prepareStatement(queryUpdate);
	    		eventFormEdit.setBoolean(1, isActive);
	    		eventFormEdit.setInt(2, eventFormID);
	    		eventFormEdit.executeUpdate();
			}
			else {
	    		String queryUpdate = "UPDATE EVENTFORMS SET NumInEvent = ?, IsActive = ? WHERE EventFormID = ?";
	    		eventFormEdit = conn.prepareStatement(queryUpdate);
	    		eventFormEdit.setInt(1, numInEvent);
	    		eventFormEdit.setBoolean(2, isActive);
	    		eventFormEdit.setInt(3, eventFormID);
	    		eventFormEdit.executeUpdate();
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
				if (eventFormEdit != null) {
					eventFormEdit.close();
				}
				if (getNumInEvent != null) {
					getNumInEvent.close();
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
	public boolean deleteEventForm(int eventFormID) throws MoodlysisInternalServerError, MoodlysisNotFound {
		// TODO Auto-generated method stub
		PreparedStatement eventFormDelete  = null;
		PreparedStatement existCheck = null;
		ResultSet table = null;
		try {
			conn.setAutoCommit(false);
			String selectQuery = "SELECT * FROM EVENTFORMS WHERE EventFormID = ?";
			existCheck = conn.prepareStatement(selectQuery);
			existCheck.setInt(1, eventFormID);
			table = existCheck.executeQuery();
			if (!table.next()) {
				throw new MoodlysisNotFound("EventForm not found. EventForm may have expired or been deleted.");
			}
    		String query = "DELETE FROM EVENTFORMS WHERE EventFormID = ?";
    		eventFormDelete = conn.prepareStatement(query);
    		eventFormDelete.setInt(1, eventFormID);
    		eventFormDelete.executeUpdate();
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
				if (eventFormDelete != null) {
					eventFormDelete.close();
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
