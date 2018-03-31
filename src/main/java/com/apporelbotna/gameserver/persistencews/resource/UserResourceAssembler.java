package com.apporelbotna.gameserver.persistencews.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import com.apporelbotna.gameserver.stubs.User;

@Component
public class UserResourceAssembler extends ResourceAssembler<User, UserResource>
{

	@Autowired
	protected EntityLinks entityLinks;

	private static final String UPDATE_REL = "update";
	private static final String DELETE_REL = "delete";



	@Override
	public UserResource toResource(User user)
	{
		UserResource userResource = new UserResource(user);

		final Link selfLink = entityLinks.linkToSingleResource(user);

		userResource.add(selfLink.withSelfRel());
		userResource.add(selfLink.withRel(UPDATE_REL));
		userResource.add(selfLink.withRel(DELETE_REL));

		return userResource;
	}

}
