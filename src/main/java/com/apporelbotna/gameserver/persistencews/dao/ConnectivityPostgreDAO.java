package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ConnectivityPostgreDAO implements Connectivity
{
    private static final Logger logger = LoggerFactory.getLogger(ConnectivityPostgreDAO.class);

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
	    logger.debug("Connected to the PostgreSQL server successfully.");
	} catch (SQLException e)
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
