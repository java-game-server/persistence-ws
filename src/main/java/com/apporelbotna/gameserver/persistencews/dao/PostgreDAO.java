package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException.Reason;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;


/**
 * TODO rename this class
 * TODO To be documented
 * Singletone
 */
public class PostgreDAO
{
	private final String url = "jdbc:postgresql://localhost/AppOrElBotnaGameClient";
	private final String dbUser = "root";
	private final String dbPassword = "root";
	private Connection conn = null;

	private static PostgreDAO instance = new PostgreDAO();

	private PostgreDAO()
	{

	}

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
	 * @param email
	 *            the primaryKey of the user.
	 * @return user if exist or null.
	 * @throws SQLException
	 */
	public User getUserBasicInformation(String email) throws SQLException
	{
		Statement st = conn.createStatement();
		String select = "SELECT email, name, password FROM public.\"user\" where email = '" + email + "'";
		ResultSet rs = st.executeQuery(select);
		User user = null;

		if (rs.next())
		{
			user = new User(
					rs.getString("email"),
					rs.getString("name")
					);
		}

		rs.close();
		st.close();
		return user;
	}

	/**
	 * metodo que te saca todos los juegos de un usuario.
	 *
	 * @param email the primary key of the User
	 * @return
	 * @throws SQLException
	 */
	public List<Game> getAllGamesByUser(String email) throws SQLException
	{
		Statement st = conn.createStatement();
		String select = "SELECT uhbg.email_user, uhbg.id_game, g.name, g.description"
				+ " FROM public.user_have_bought_game uhbg"
				+ " INNER Join game g on g.id = uhbg.id_game"
				+ " where email_user = '" + email + "'";
		ResultSet rs = st.executeQuery(select);

		List<Game> userGames = new ArrayList<>();

		while (rs.next())
		{
			userGames.add(new Game(
					rs.getInt("id_game"),
					rs.getString("name"),
					rs.getString("description")
					)
					);
		}

		rs.close();
		st.close();

		return userGames;
	}

	/**
	 * TODO mirar si se puede combinar con la funcion getUserInformation sin guardar
	 * en el stub User la password
	 *
	 * @param email
	 * @return the password of the user if exists or null
	 * @throws SQLException
	 */
	public String getUserPassword(String email) throws SQLException
	{
		String select = "SELECT password FROM public.user where email = '" + email + "'";
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

	public float getHourPlayedInGame(String email, int gameId) throws SQLException, InvalidInformationException
	{
		if(getUserBasicInformation(email) == null) {
			throw new InvalidInformationException(Reason.USER_IS_NOT_STORED);
		}

		if(getGameById(gameId) == null) {
			throw new InvalidInformationException(Reason.GAME_IS_NOT_STORED);
		}

		String select = "select email_user, id_game, game_lenght "
				+ "from public.user_historical_game "
				+ "where email_user = '" + email + "' and id_game = " + gameId;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(select);

		float totalTime = 0.0f;
		while (rs.next())
		{
			totalTime += rs.getFloat("game_lenght");
		}

		rs.close();
		st.close();

		return totalTime;
	}

	public Game getGameById(int idGame) throws SQLException
	{
		Statement st = conn.createStatement();
		String select = "select * from public.game where id = " + idGame;
		ResultSet rs = st.executeQuery(select);

		Game game = null;
		if (rs.next())
		{
			game = new Game(rs.getInt("id"),
							rs.getString("name"),
							rs.getString("description")
							);
		}

		rs.close();
		st.close();
		return game;
	}

	public List<RankingPointsTO> getRankingUsersGameByPoints(int idGame) throws SQLException, InvalidInformationException
	{
		Game game = getGameById(idGame);

		if(game == null) {
			throw new InvalidInformationException(Reason.GAME_IS_NOT_STORED);
		}

		Statement st = conn.createStatement();
		String select = "select email_user, sum(puntuation) as puntuation"
				+ " from public.user_historical_game"
				+ " where id_game = " + idGame
				+ " group by email_user";
		ResultSet rs = st.executeQuery(select);

		List<RankingPointsTO> ranking = new ArrayList<>();
		while (rs.next())
		{
			ranking.add(
					new RankingPointsTO(
							rs.getString("email_user"),
							rs.getRow(),
							rs.getInt("puntuation")
							)
					);
		}

		rs.close();
		st.close();

		return ranking;
	}


	/*************************** Insert ********************************/

	/**
	 * @param user
	 * @param password
	 * @throws InvalidInformationException
	 * @throws SQLException
	 */
	public void storeNewUserInBBDD(User user, String password) throws InvalidInformationException, SQLException
	{
		if (getUserBasicInformation(user.getEmail()) != null)   // TODO make method that returns boolean if user exist
																//	or not and remove this CheiPuZa
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

	/**
	 * Store new token to one user.
	 * It checks if the user exist
	 *
	 * @param user
	 * @param token
	 * @throws InvalidInformationException if the user is stored
	 * @throws SQLException
	 */
	public void storeTokenToUser(User user, Token token) throws InvalidInformationException, SQLException
	{
		if (getUserBasicInformation(user.getEmail()) == null)
		{
			throw new InvalidInformationException(Reason.USER_IS_NOT_STORED);
		}

		String query = "INSERT INTO public.token (token, user_email) VALUES (?, ?);";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, token.getTokenName());
		preparedStatement.setString(2, user.getEmail());
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}


	/**
	 * @param Match
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	public void storeNewMatch(Match Match) throws SQLException, InvalidInformationException
	{
		User user = getUserBasicInformation(Match.getEmailUser());
		if(user == null) {
			throw new InvalidInformationException(Reason.USER_IS_NOT_STORED);
		}

		Game game = getGameById(Match.getIdGame());
		if(game == null) {
			throw new InvalidInformationException(Reason.GAME_IS_NOT_STORED);
		}

		String query = "INSERT INTO public.user_historical_game"
				+ " (email_user, id_game, game_lenght, puntuation)"
				+ " VALUES (?, ?, ?, ?);";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, Match.getEmailUser());
		preparedStatement.setInt(2, Match.getIdGame());
		preparedStatement.setFloat(3, Match.getGameLenght());
		preparedStatement.setInt(4, Match.getPuntuation());
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

}
