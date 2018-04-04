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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((identifiable == null) ? 0 : identifiable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!super.equals(obj))
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		GameResource other = (GameResource) obj;
		if (identifiable == null)
		{
			if (other.identifiable != null)
			{
				return false;
			}
		} else if (!identifiable.equals(other.identifiable))
		{
			return false;
		}
		return true;
	}




}
