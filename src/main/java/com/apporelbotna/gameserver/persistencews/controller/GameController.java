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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.persistencews.resource.GameResource;
import com.apporelbotna.gameserver.persistencews.resource.GameResourceAssembler;
import com.apporelbotna.gameserver.stubs.Game;

@RestController
@ExposesResourceFor(Game.class)
@RequestMapping(value = "/game", produces = "application/json")
public class GameController
{

    @Autowired
    private PostgreDAO postgreDAO;

    @Autowired
    private GameResourceAssembler assembler;

    @RequestMapping(value = "/{email}", method = RequestMethod.GET)
    public ResponseEntity< Collection< GameResource > > findAllGames(@PathVariable String email)
    {

	postgreDAO.connect();
	List< Game > games = new ArrayList<>();
	try
	{
	    games = postgreDAO.getAllGamesByUser( email );
	} catch ( SQLException e )
	{
	    e.printStackTrace();
	}
	return new ResponseEntity<>( assembler.toResourceCollection( games ),
				     HttpStatus.OK );

    }

}
