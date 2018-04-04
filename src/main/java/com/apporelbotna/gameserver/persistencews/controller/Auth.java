package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;
import com.apporelbotna.gameserver.stubs.UserWrapper;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
public class Auth
{
	PostgreDAO postgreDAO = new PostgreDAO();


//	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
//	public @ResponseBody ResponseEntity<?> isUserLoggeable(@RequestBody UserWrapper wrapper)
//	{
//		postgreDAO.connect();
//
//		try
//		{
//			if(postgreDAO.isTokenValid(wrapper)) {
//				return new ResponseEntity<>( HttpStatus.OK);
//			}
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		} catch (SQLException e1)
//		{
//			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
//		}
//	}

	@RequestMapping(value = "/{email}/{token}", method = RequestMethod.GET)
	public ResponseEntity<?> isUserLoggeable(@PathVariable("email") String email, @PathVariable("token") String token)
	{
		postgreDAO.connect();
		try
		{
			Token tokenName = new Token(token);
			User user = new User(email, "asd");

			UserWrapper wrapper = new UserWrapper();
			wrapper.setUser(user);
			wrapper.setToken(tokenName);

			if(postgreDAO.isTokenValid(wrapper)) {
				return new ResponseEntity<>( HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (SQLException e1)
		{
			return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
		}
	}


}
