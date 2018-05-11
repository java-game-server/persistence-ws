package com.apporelbotna.gameserver.persistencews.resource;

import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.apporelbotna.gameserver.stubs.Game;
import com.apporelbotna.gameserver.stubs.User;

public class UserResource extends ResourceSupport
{
    private final String email;
    private final String name;
    private final List< Game > games;

    public UserResource(
			User user)
    {
	this.name = user.getName();
	this.email = user.getId();
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

    public List< Game > getGames()
    {
	return games;
    }

    @Override
    public int hashCode()
    {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ( ( email == null ) ? 0 : email.hashCode() );
	return result;
    }

    @Override
    public boolean equals(Object obj)
    {
	if ( this == obj )
	{
	    return true;
	}
	if ( !super.equals( obj ) )
	{
	    return false;
	}
	if ( getClass() != obj.getClass() )
	{
	    return false;
	}
	UserResource other = ( UserResource ) obj;
	if ( email == null )
	{
	    if ( other.email != null )
	    {
		return false;
	    }
	} else if ( !email.equals( other.email ) )
	{
	    return false;
	}
	return true;
    }

}
