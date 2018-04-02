package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException.Reason;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;
import com.apporelbotna.gameserver.stubs.UserWrapper;


/**
 * TODO To be documented
 */
@Repository
public class PostgreDAO extends ConnectivityPostgreDAO implements DAO
{
	public PostgreDAO()
	{

	}

	/*************************** Selects ***********************************/

	/**
	 * @param email
	 *            the primaryKey of the user.
	 * @return user if exist or null.
	 * @throws SQLException
	 */
	@Override
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
	@Override
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
	@Override
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

	@Override
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

	@Override
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

	@Override
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
	 * @param userWrapper
	 * @param password
	 * @throws InvalidInformationException
	 * @throws SQLException
	 */
	@Override
	public void storeNewUserInBBDD(UserWrapper userWrapper) throws InvalidInformationException, SQLException
	{
		User user = userWrapper.getUser();
		if (getUserBasicInformation(user.getId()) != null)   // TODO make method that returns boolean if user exist
																//	or not and remove this CheiPuZa
		{
			throw new InvalidInformationException(Reason.USER_IS_STORED);
		}

		String query = "INSERT INTO public.\"user\"(email, name, password)	VALUES (?, ?, ?)";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, user.getId());
		preparedStatement.setString(2, user.getName());
		preparedStatement.setString(3, userWrapper.getPassword());
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
	@Override
	public void storeTokenToUser(User user, Token token) throws InvalidInformationException, SQLException
	{
		if (getUserBasicInformation(user.getId()) == null)
		{
			throw new InvalidInformationException(Reason.USER_IS_NOT_STORED);
		}

		String query = "INSERT INTO public.token (token, user_email) VALUES (?, ?);";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, token.getTokenName());
		preparedStatement.setString(2, user.getId());
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}


	/**
	 * @param match
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	@Override
	public void storeNewMatch(Match match) throws SQLException, InvalidInformationException
	{
		User user = getUserBasicInformation(match.getEmailUser());
		if(user == null) {
			throw new InvalidInformationException(Reason.USER_IS_NOT_STORED);
		}

		Game game = getGameById(match.getIdGame());
		if(game == null) {
			throw new InvalidInformationException(Reason.GAME_IS_NOT_STORED);
		}

		String query = "INSERT INTO public.user_historical_game"
				+ " (email_user, id_game, game_lenght, puntuation)"
				+ " VALUES (?, ?, ?, ?);";

		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, match.getEmailUser());
		preparedStatement.setInt(2, match.getIdGame());
		preparedStatement.setFloat(3, match.getGameLenght());
		preparedStatement.setInt(4, match.getPuntuation());
		preparedStatement.executeUpdate();
		preparedStatement.close();
	}

}
