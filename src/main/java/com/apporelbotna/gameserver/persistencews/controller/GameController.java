package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import com.apporelbotna.gameserver.persistencews.resource.GameResource;
import com.apporelbotna.gameserver.persistencews.resource.GameResourceAssembler;
import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.User;

@RestController
@ExposesResourceFor(Game.class)
@RequestMapping(value = "/game", produces = "application/json")
public class GameController
{

    @Autowired
    private PostgreDAO postgreDAO;

    @Autowired
    private GameResourceAssembler assembler;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity< Collection< GameResource > > findAllGames()
    {
	postgreDAO.connect();
	List< Game > games = new ArrayList<>();
	try
	{
	    games = postgreDAO.getAllGames();
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    e.printStackTrace();
	}
	return new ResponseEntity<>( assembler.toResourceCollection( games ), HttpStatus.OK );
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public @ResponseBody ResponseEntity< ? > createGame(@RequestBody Game gameToStore)
    {
	try
	{
	    postgreDAO.connect();
	    int idGenerated = postgreDAO.storeNewGame( gameToStore );
	    postgreDAO.getGameById( idGenerated );

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

    @RequestMapping(value = "/{genre}", method = RequestMethod.PUT, consumes = "application/json")
    public @ResponseBody ResponseEntity< ? > createGame(@PathVariable("genre") String genre, @RequestBody Game game)
    {
	try
	{
	    postgreDAO.connect();
	    postgreDAO.storeGenreToGame(game, genre);
	    Game gameById = postgreDAO.getGameById( game.getId() );
	    System.out.println( gameById );

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

    @RequestMapping(value = "/{userInput}/name", method = RequestMethod.GET)
    public ResponseEntity< List<Game> > findGamesLikeName(@PathVariable String userInput)
    {
	postgreDAO.connect();
	try
	{
	    List<Game> games = postgreDAO.findGamesLikeName( userInput );
	    return new ResponseEntity<>( games, HttpStatus.OK );

	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return null;
	}
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<?> addGameToUser(@PathVariable("id") int id, @RequestBody User user)
    {
        try
	{
	    postgreDAO.storeGameToUser( user, id );
	} catch ( SQLException | InvalidInformationException e )
	{
	    System.out.println( e.getMessage() );
	    e.printStackTrace();
	}
        return new ResponseEntity<>( HttpStatus.OK);
    }

}
