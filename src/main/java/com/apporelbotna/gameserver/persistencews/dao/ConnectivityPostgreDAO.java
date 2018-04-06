package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.apporelbotna.gameserver.persistencews.properties.ApplicationProperties;

@Repository
public class ConnectivityPostgreDAO implements Connectivity
{
	private static final Logger logger = LoggerFactory.getLogger(ConnectivityPostgreDAO.class);

	protected Connection conn;

	@Override
	public Connection connect()
	{
		try
		{
			conn = DriverManager.getConnection(
					ApplicationProperties.getDatabaseUrl(),
					ApplicationProperties.getDatabaseUser(),
					ApplicationProperties.getDatabasePassword());
			logger.debug("Connected to the PostgreSQL server successfully.");
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage());
		}
		return conn;
	}

	@Override
	public void close()
	{
		conn = null;
	}
}
