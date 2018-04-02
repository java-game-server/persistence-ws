package com.apporelbotna.gameserver.persistencews.resource;

import org.springframework.hateoas.ResourceSupport;

import com.apporelbotna.gameserver.stubs.Game;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResource extends ResourceSupport
{
	private final Integer identifiable;
	private final String name;
	private final String description;

	public GameResource(
						Game game)
	{
		this.identifiable = game.getId();
		this.name = game.getName();
		this.description = game.getDescription();
	}

	@JsonProperty("id")
	public Integer getIdentifialbe()
	{
		return identifiable;
	}


	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}


}
