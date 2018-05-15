package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.persistencews.service.UserService;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.RegisterUser;
import com.apporelbotna.gameserver.stubs.User;

@RestController
@ExposesResourceFor(User.class)
@RequestMapping(value = "/user", produces = "application/json")
public class UserController
{
    @Autowired
    private PostgreDAO postgreDAO;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public ResponseEntity< User > findUserByEmail(@PathVariable String email)
    {
	User user = null;
	try
	{
	    user = userService.getAllInformationUser( email );
	    return new ResponseEntity<>( user, HttpStatus.OK );
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.NOT_FOUND );
	}
    }

    @RequestMapping(value = "{email}/game/{game}/time/", method = RequestMethod.GET)
    public ResponseEntity< Float > getTimePlayedByUsername(@PathVariable String email, @PathVariable int game)
    {
	postgreDAO.connect();
	try
	{
	    float time = postgreDAO.getTimePlayedInGame( email, game );
	    return new ResponseEntity<>( time, HttpStatus.ACCEPTED );

	} catch ( SQLException | InvalidInformationException e )
	{
	    System.out.println( e.getMessage() );
	    return null;
	}
    }

    @RequestMapping(value = "{userInput}/name", method = RequestMethod.GET)
    public ResponseEntity< List< User > > findUsersLikeName(@PathVariable String userInput)
    {
	postgreDAO.connect();
	try
	{
	    List< User > users = postgreDAO.findUsersLikeName( userInput );
	    return new ResponseEntity<>( users, HttpStatus.OK );

	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return null;
	}
    }

    @RequestMapping(value = "{userInput}/email", method = RequestMethod.GET)
    public ResponseEntity< List< User > > findUsersLikeEmail(@PathVariable String userInput)
    {
	postgreDAO.connect();
	try
	{
	    List< User > users = postgreDAO.findUsersLikeEmail( userInput );
	    return new ResponseEntity<>( users, HttpStatus.OK );

	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return null;
	}
    }

    /**
     * Create a new User
     *
     * @param user
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity< ? > createUser(@RequestBody RegisterUser userToRegister)
    {
	try
	{
	    postgreDAO.connect();
	    postgreDAO.storeNewUserInBBDD( userToRegister );
	    userService.getAllInformationUser( userToRegister.getUser().getId() );

	    return new ResponseEntity<>( HttpStatus.CREATED );

	} catch ( InvalidInformationException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.ALREADY_REPORTED );
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.SERVICE_UNAVAILABLE );
	}
    }

    @RequestMapping(value = "/game/{userEmail}", method = RequestMethod.GET)
    public ResponseEntity< List <Game> > findAllGamesByUser(@PathVariable String userEmail)
    {
	postgreDAO.connect();
	List< Game > games = new ArrayList<>();
	try
	{
	    games = postgreDAO.getAllGamesByUser( userEmail );

	    return new ResponseEntity<>( games , HttpStatus.OK );
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
	}
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity< ? > updateUserBasicInformation(@RequestBody User user)
    {
	postgreDAO.connect();
	try
	{
	    postgreDAO.updateUser( user );
	    return new ResponseEntity<>( HttpStatus.OK );
	} catch ( SQLException | InvalidInformationException e )
	{
	    System.out.println( e.getMessage() );
	    e.printStackTrace();
	    return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR );
	}
    }

}
