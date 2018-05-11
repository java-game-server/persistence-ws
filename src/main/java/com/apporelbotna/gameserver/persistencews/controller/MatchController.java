package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.Match;

@RestController
@ExposesResourceFor(Match.class)
@RequestMapping(
		value = "/match",
		produces = "application/json")
public class MatchController
{

    @Autowired
    private PostgreDAO postgreDAO;

    @RequestMapping(
		    method = RequestMethod.POST,
		    consumes = "application/json")
    public @ResponseBody ResponseEntity< ? > storeMatchGame(@RequestBody Match match)
    {

	postgreDAO.connect();
	try
	{
	    postgreDAO.storeNewMatch( match );
	    System.out.println( "ok" );
	    return new ResponseEntity<>( HttpStatus.CREATED );

	} catch ( InvalidInformationException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.CONFLICT );
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    return new ResponseEntity<>( HttpStatus.SERVICE_UNAVAILABLE );
	}

    }
}
