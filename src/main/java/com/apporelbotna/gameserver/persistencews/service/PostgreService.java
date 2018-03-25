package com.apporelbotna.gameserver.persistencews.service;

import java.sql.SQLException;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.User;

//TODO rename this class
/**
 * Singletone
 */
public class PostgreService
{
	private static PostgreService instance = new PostgreService();

	private PostgreService()
	{

	}

	public static PostgreService getInstance()
	{
		return instance;
	}


	public User getAllInformationUser(String email) throws SQLException {
		PostgreDAO postgreDao = PostgreDAO.getInstance();
		User user = postgreDao.getUserBasicInformation(email);

		if(user != null) {
			user.setGames(postgreDao.getGamesUser(email));
		}

		return user;
	}



}
