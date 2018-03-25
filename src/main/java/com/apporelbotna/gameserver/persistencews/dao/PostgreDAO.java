package com.apporelbotna.gameserver.persistencews;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreDAO
{
	private final String url = "jdbc:postgresql://localhost/AppOrElBotnaGameClient";
	private final String user = "root";
	private final String password = "root";

	 public PostgreDAO() {
		 
	 }
	 
	 public Connection connect() {
	        Connection conn = null;
	        try {
	            conn = DriverManager.getConnection(url, user, password);
	            System.out.println("Connected to the PostgreSQL server successfully.");
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	 
	        return conn;
	    }
}
