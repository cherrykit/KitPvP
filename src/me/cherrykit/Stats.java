package me.cherrykit;

import java.sql.*;

public class Stats {

	private static Connection c;
	
	//Gets connection to database
	public static void getConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:stats.db");
			System.out.println("Successfully connected to database");
		} catch (Exception e) {
			System.out.println("Error while connecting: " + e);
		}
	}
	
	//Returns stats of given player
	public static String[] getStats(String pname) {
		String[] results = new String[2];
		try {
			Statement stmt = c.createStatement();
			String query = "select * from STATS where playername = '" + pname + "'";
			ResultSet rs = stmt.executeQuery(query);
			
			if (rs.next()) {
				results[0] = rs.getString("kills");
				results[1] = rs.getString("deaths");
			} else {
				results[0] = "0";
				results[1] = "0";
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
			results[0] = "0";
			results[1] = "0";
		}
		return results;
	}
	
	//Sets stats of given player
	public static void setStats(String pname, String kills, String deaths) {
		try {
			Statement stmt = c.createStatement();
			String[] current = getStats(pname);
			
			//If player not yet in database
			if (current[0] == "0" && current[1] == "0") {
				System.out.println("Creating column for " + pname);
				String query = "insert into STATS (playername, kills, deaths) values ('" + 
				       pname + "' ," + kills + " ," + deaths + ")";
				stmt.executeUpdate(query);
			} 
			//If player aleady in database
			else {
				String query = "update STATS set kills = " + kills + " where playername = '" + pname + "'";
				stmt.executeUpdate(query);
				query = "update STATS set deaths = " + deaths + " where playername = '" + pname + "'";
				stmt.executeUpdate(query);
			}
			
			stmt.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	//Closes connection to database
	public static void closeConnection() {
		try {
			c.close();
		} catch (Exception e) {
			System.out.println("Error while closing connection to database: " + e);
		}
	}
}
