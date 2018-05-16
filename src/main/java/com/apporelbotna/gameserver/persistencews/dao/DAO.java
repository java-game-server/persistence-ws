package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.SQLException;
import java.util.List;

import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;
import com.apporelbotna.gameserver.stubs.RegisterUser;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;
import com.apporelbotna.gameserver.stubs.UserWrapper;

public interface DAO
{
	/**
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	User getUserBasicInformation(String email) throws SQLException;

	/**
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	List<Game> getAllGamesByUser(String email) throws SQLException;

	/**
	 * @param email
	 * @return
	 * @throws SQLException
	 */
	String getUserPassword(String email) throws SQLException;

	/**
	 * @param email
	 * @param gameId
	 * @return
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	float getTimePlayedInGame(String email, int gameId) throws SQLException, InvalidInformationException;

	/**
	 * @param idGame
	 * @return
	 * @throws SQLException
	 */
	Game getGameById(int idGame) throws SQLException;

	/**
	 * @param idGame
	 * @return
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	List<RankingPointsTO> getRankingUsersGameByPoints(int idGame) throws SQLException, InvalidInformationException;

	/**
	 * @param user
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	void updateUser(User user) throws SQLException, InvalidInformationException;

	/**
	 * @param userToRegister
	 * @throws InvalidInformationException
	 * @throws SQLException
	 */
	void storeNewUserInBBDD(RegisterUser userToRegister) throws InvalidInformationException, SQLException;

	/**
	 * @param user
	 * @param token
	 * @throws InvalidInformationException
	 * @throws SQLException
	 */
	void storeTokenToUser(User user, Token token) throws InvalidInformationException, SQLException;

	/**
	 * @param match
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	void storeNewMatch(Match match) throws SQLException, InvalidInformationException;

	/**
	 * @param userWrapper
	 * @return
	 * @throws SQLException
	 */
	boolean isTokenValid(UserWrapper userWrapper) throws SQLException;

	/**
	 * @param game
	 * @throws SQLException
	 * @throws InvalidInformationException
	 */
	public int storeNewGame(Game game) throws SQLException, InvalidInformationException;
}
