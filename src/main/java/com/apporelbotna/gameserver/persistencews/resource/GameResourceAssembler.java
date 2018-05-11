package com.apporelbotna.gameserver.persistencews.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.apporelbotna.gameserver.stubs.Game;

@Component
public class GameResourceAssembler extends ResourceAssembler< Game , GameResource >
{
    @Autowired
    protected EntityLinks entityLinks;

    private static final String UPDATE_REL = "update";
    private static final String DELETE_REL = "delete";

    @Override
    public GameResource toResource(Game game)
    {
	GameResource gameResource = new GameResource( game );

	final Link selfLink = entityLinks.linkToSingleResource( game );

	gameResource.add( selfLink.withSelfRel() );
	gameResource.add( selfLink.withRel( UPDATE_REL ) );
	gameResource.add( selfLink.withRel( DELETE_REL ) );

	return gameResource;
    }

}
