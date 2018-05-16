package com.apporelbotna.gameserver.persistencews.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.InvalidInformationException;
import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.RankingPointsTO;

@RestController
@ExposesResourceFor(RankingController.class)
@RequestMapping(value = "/ranking", produces = "application/json")
public class RankingController
{
	@Autowired
	private PostgreDAO postgreDAO;

	@RequestMapping(value = "/{game}", method = RequestMethod.GET)
	public ResponseEntity<List<RankingPointsTO>> getRankingPointsByGame(@PathVariable int game)
	{
		postgreDAO.connect();
		List<RankingPointsTO> ranking = new ArrayList<>();

		try
		{
			ranking = postgreDAO.getRankingUsersGameByPoints(game);
		} catch (InvalidInformationException | SQLException e)
		{
			e.printStackTrace();
		}
		return new ResponseEntity<>(ranking, HttpStatus.OK);

	}

}
