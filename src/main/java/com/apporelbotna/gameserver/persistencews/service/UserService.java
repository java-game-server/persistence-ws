package com.apporelbotna.gameserver.persistencews.service;

import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.User;

@Service
public class UserService
{
	private PostgreDAO postgreDAO;

	public UserService()
	{
		postgreDAO = new PostgreDAO();
	}



	public User getAllInformationUser(String email) throws SQLException {
		PostgreDAO postgreDao = new PostgreDAO();
		User user = postgreDao.getUserBasicInformation(email);

		if(user != null) {
			user.setGames(postgreDao.getAllGamesByUser(email));
		}

		return user;
	}
}
