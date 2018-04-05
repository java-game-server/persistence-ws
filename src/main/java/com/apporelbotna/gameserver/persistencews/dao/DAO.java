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
	User getUserBasicInformation(String email) throws SQLException;

	List<Game> getAllGamesByUser(String email) throws SQLException;

	String getUserPassword(String email) throws SQLException;

	float getTimePlayedInGame(String email, int gameId)
			throws SQLException, InvalidInformationException;

	Game getGameById(int idGame) throws SQLException;

	List<RankingPointsTO> getRankingUsersGameByPoints(int idGame)
			throws SQLException, InvalidInformationException;

	void storeNewUserInBBDD(RegisterUser userToRegister)
			throws InvalidInformationException, SQLException;

	void storeTokenToUser(User user, Token token) throws InvalidInformationException, SQLException;

	void storeNewMatch(Match match) throws SQLException, InvalidInformationException;

	boolean isTokenValid(UserWrapper userWrapper) throws SQLException;
}
