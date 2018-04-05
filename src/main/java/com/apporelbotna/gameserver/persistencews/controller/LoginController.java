package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.UUID;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;

@RestController
@ExposesResourceFor(Token.class)
@RequestMapping(value = "/login", produces = "application/json")
public class LoginController
{
	PostgreDAO postgreDAO = new PostgreDAO();

	@RequestMapping(value = "/{email}/{password}", method = RequestMethod.GET)
	public Token login(@PathVariable("email") String email,
			@PathVariable("password") String password)
	{
		postgreDAO.connect();
		try
		{
			String passwordUserDB = postgreDAO.getUserPassword(email);
			if (passwordUserDB.equals(password))
			{
				Token token = generateToken();
				postgreDAO.storeTokenToUser(new User(email), token);
				return token;
			}

		} catch (SQLException | InvalidInformationException e)
		{
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * Apply an algorithm to create a new token.
	 *
	 * @return The generated token
	 */
	private Token generateToken()
	{
		String randomToken = UUID.randomUUID().toString();
		randomToken = randomToken.replaceAll("-", "");
		return new Token(randomToken);
	}
}
