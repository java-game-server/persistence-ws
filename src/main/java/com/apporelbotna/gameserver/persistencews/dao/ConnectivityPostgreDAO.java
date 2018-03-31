package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

@Repository
public class ConnectivityPostgreDAO implements Connectivity
{

	private static final String URL = "jdbc:postgresql://localhost/AppOrElBotnaGameClient";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "root";
	protected Connection conn;

	@Override
	public Connection connect()
	{
		try
		{
			conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return conn;
	}

	@Override
	public void close()
	{
		conn = null;
	}
}
