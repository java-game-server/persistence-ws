package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException.Reason;
import com.apporelbotna.gameserver.stubs.User;

public class PostgreDAO
{
	private final String url = "jdbc:postgresql://localhost/AppOrElBotnaGameClient";
	private final String dbUser = "root";
	private final String dbPassword = "root";
	private Connection conn = null;

	private PostgreDAO()
	{
	}

	private static PostgreDAO instance = new PostgreDAO();

	public static PostgreDAO getInstance()
	{
		return instance;
	}

	public Connection connect()
	{
		try
		{
			conn = DriverManager.getConnection(url, dbUser, dbPassword);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public void close()
	{
		conn = null;
	}

	/*************************** Selects ***********************************/

	/**
	 * @param email the primaryKey of the user.
	 * @return user if exist or null.
	 * @throws SQLException
	 */
	public User getUserInformation(String email) throws SQLException
	{
		String select = "SELECT email, name, password FROM public.\"user\" where email = '" + email + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(select);
		User user = null;

		if (rs.next())
		{
			user = new User(rs.getString("email"), rs.getString("name"));
		}

		rs.close();
		st.close();
		return user;
	}

	/**
	 *TODO mirar si se puede combinar con la funcion getUserInformation sin guardar en el stub User la password
	 *
	 * @param email
	 * @return the password of the user if exists or null
	 * @throws SQLException
	 */
	public String getUserPassword(String email) throws SQLException
	{

		String select = "SELECT password FROM public.\"user\" where email = '" + email + "'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(select);
		String password = null;
		if (rs.next())
		{
			password = rs.getString("password");
		}

		rs.close();
		st.close();
		return password;

	}

	/*************************** Insert ***********************************/

	/**
	 * @param user
	 * @param password
	 * @throws InvalidInformationException
	 * @throws SQLException
	 */
	public void StoreNewUserInBBDD(User user, String password) throws InvalidInformationException, SQLException
	{
		if (getUserInformation(user.getEmail()) != null)
		{
			throw new InvalidInformationException(Reason.USER_IS_STORED);
		}
		String query = "INSERT INTO public.\"user\"(email, name, password)	VALUES (?, ?, ?)";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, user.getEmail());
		preparedStatement.setString(2, user.getName());
		preparedStatement.setString(3, password);

		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

}
