package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.persistencews.resource.GameResource;
import com.apporelbotna.gameserver.persistencews.resource.GameResourceAssembler;
import com.apporelbotna.gameserver.persistencews.resource.UserResource;
import com.apporelbotna.gameserver.persistencews.resource.UserResourceAssembler;
import com.apporelbotna.gameserver.persistencews.service.UserService;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;
import com.apporelbotna.gameserver.stubs.UserWrapper;

@CrossOrigin(origins = "*")
@RestController
@ExposesResourceFor(User.class)
@RequestMapping(value = "/user", produces = "application/json")
public class UserController
{
	@Autowired
	private PostgreDAO postgreDAO;

	@Autowired
	private UserService userService;

	@Autowired
	private UserResourceAssembler userAssembler;

	@Autowired
	private GameResourceAssembler gameAssembler;

	@RequestMapping(value = "/{email}", method = RequestMethod.GET)
	public ResponseEntity<UserResource> findUserByEmail(@PathVariable String email)
	{
		User user = null;
		try
		{
			user = userService.getAllInformationUser(email);
			return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);

		} catch (SQLException e)
		{
			// Log
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Create a new User TODO return a resource
	 *
	 * @param user
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody HttpStatus createUser(@RequestBody UserWrapper user)
	{
		System.out.println(user);
		try
		{
			postgreDAO.connect();
			postgreDAO.storeNewUserInBBDD(user);
			userService.getAllInformationUser(user.getUser().getId());

			return HttpStatus.CREATED;

		} catch (InvalidInformationException e)
		{
			return HttpStatus.CONFLICT;
		} catch (SQLException e)
		{
			return HttpStatus.SERVICE_UNAVAILABLE;
		}
	}

	@RequestMapping(value = "/game/{email}", method = RequestMethod.GET)
	public ResponseEntity<Collection<GameResource>> findAllGamesByUser(@PathVariable User user)
	{
		postgreDAO.connect();
		List<Game> games = new ArrayList<>();
		try
		{
			games = postgreDAO.getAllGamesByUser(user.getId());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(gameAssembler.toResourceCollection(games), HttpStatus.OK);

	}

	@RequestMapping(value = "/user/game/hour/{email}", method = RequestMethod.GET)
	public ResponseEntity<Collection<GameResource>> findHourPlayedByUsername(@PathVariable String email)
	{
		// TODO implement
		return null;
	}

	@RequestMapping(value = "/{email}", method = RequestMethod.POST)
	public ResponseEntity<Token> login(@PathVariable UserWrapper userWrapper)
	{
		postgreDAO.connect();
		try
		{
			String passwordUserDB = postgreDAO.getUserPassword(userWrapper.getPassword());
			if(passwordUserDB.equals(userWrapper.getPassword()))
			{
				Token token = new Token();
				postgreDAO.storeTokenToUser(userWrapper.getUser(), token);
				return ResponseEntity<>(token, HttpStatus.ACCEPTED);
			}

		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
