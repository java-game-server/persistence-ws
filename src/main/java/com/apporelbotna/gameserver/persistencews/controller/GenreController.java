package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;

@RestController
@ExposesResourceFor(String.class)
@RequestMapping(value = "/genre", produces = "application/json")
public class GenreController
{
    @Autowired
    private PostgreDAO postgreDAO;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity< List<String> > findAllGenres()
    {
	postgreDAO.connect();
	List< String > genres = new ArrayList<>();
	try
	{
	    genres = postgreDAO.getAllGenre();
	} catch ( SQLException e )
	{
	    System.out.println( e.getMessage() );
	    e.printStackTrace();
	}
	return new ResponseEntity<>(genres, HttpStatus.OK );
    }
}
