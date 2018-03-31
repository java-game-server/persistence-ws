package com.apporelbotna.gameserver.persistencews.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.Token;
import com.apporelbotna.gameserver.stubs.User;

public class UserResource extends ResourceSupport
{
	private final String email;
	private final String name;
	private final List<Token> tokens;
	private final List<Game> games;

	public UserResource(User user)
	{
		this.name = user.getName();
		this.email = user.getId();
		this.tokens = user.getToken();
		this.games = user.getGames();
	}


	public String getEmail()
	{
		return email;
	}

	public String getName()
	{
		return name;
	}

	public List<Token> getTokens()
	{
		return tokens;
	}

	public List<Game> getGames()
	{
		return games;
	}
}
