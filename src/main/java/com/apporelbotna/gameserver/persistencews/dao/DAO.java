package com.apporelbotna.gameserver.persistencews.dao;

import java.sql.SQLException;
import java.util.List;

import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;

public interface DAO
{
	public User getUserBasicInformation(String email) throws SQLException;
	public List<Game> getAllGamesByUser(String email) throws SQLException;
	public String getUserPassword(String email) throws SQLException;
	public float getHourPlayedInGame(String email, int gameId) throws SQLException, InvalidInformationException;
	public Game getGameById(int idGame) throws SQLException;
	public List<RankingPointsTO> getRankingUsersGameByPoints(int idGame) throws SQLException, InvalidInformationException;
	public void storeNewUserInBBDD(User user, String password) throws InvalidInformationException, SQLException;
	public void storeTokenToUser(User user, Token token) throws InvalidInformationException, SQLException;
	public void storeNewMatch(Match match) throws SQLException, InvalidInformationException;
}
