package com.moodylsis.moodbe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	private static Connection conn;
	
	public static Connection getConnection() {
		if (conn == null) {
			
			try {
				Class.forName("org.postgresql.Driver");
				String dburl = "jdbc:postgresql://database.mood-net:5432/mood?user=mooduser&password=password";
				conn = DriverManager.getConnection(dburl);
			} catch (ClassNotFoundException e) {
				System.err.println("Unable to find class org.postgresql.Driver");
				e.printStackTrace();
				return null;
			} catch (SQLException e) {
				System.err.println("Unable to establish database connection");
				e.printStackTrace();
				return null;
			}
		}
	
		return conn;
		
	}
}
