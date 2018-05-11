package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;

import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.UserWrapper;

@RestController
@ExposesResourceFor(UserWrapper.class)
@RequestMapping(
		value = "/auth",
		produces = "application/json")
public class Auth
{
    PostgreDAO postgreDAO = new PostgreDAO();

    @RequestMapping(
		    method = RequestMethod.POST,
		    consumes = "application/json")
    public @ResponseBody ResponseEntity< ? > isUserLoggeable(@RequestBody UserWrapper wrapper)
    {
	postgreDAO.connect();

	try
	{
	    if ( postgreDAO.isTokenValid( wrapper ) )
	    {
		return new ResponseEntity<>( HttpStatus.OK );
	    }
	    return new ResponseEntity<>( HttpStatus.NOT_FOUND );
	} catch ( SQLException e )
	{
	    System.out.println( e );
	    return new ResponseEntity<>( HttpStatus.SERVICE_UNAVAILABLE );
	}
    }
}
