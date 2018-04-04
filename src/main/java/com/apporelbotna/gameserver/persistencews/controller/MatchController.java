package com.apporelbotna.gameserver.persistencews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.stubs.Match;
import com.apporelbotna.gameserver.stubs.User;

@ExposesResourceFor(Match.class)
@RequestMapping(value = "/match", produces = "application/json")
public class MatchController
{

	@Autowired
	private PostgreDAO postgreDAO;

	public void storeMatchGame(User player1, User player2)
	{

	}
}
