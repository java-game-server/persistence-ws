package com.apporelbotna.gameserver.persistencews.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.apporelbotna.gameserver.persistencews.dao.PostgreDAO;
import com.apporelbotna.gameserver.persistencews.resource.UserResource;
import com.apporelbotna.gameserver.persistencews.resource.UserResourceAssembler;
import com.apporelbotna.gameserver.stubs.User;

@CrossOrigin(origins = "*")
@RestController
@ExposesResourceFor(User.class)
@RequestMapping(value = "/user", produces = "application/json")
public class UserController
{
	@Autowired
	private PostgreDAO postgreDAO;

	@Autowired
	private UserResourceAssembler assembler;

//	@RequestMapping(method = RequestMethod.GET)
//	public ResponseEntity<UserResource> findUser(){
//		User user = new User("pepe@pepe.com", "aa");
//
//
//	return new ResponseEntity<>(assembler.toResource(user),HttpStatus.OK);
//	}
//

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<UserResource> aa()
	{
		User user = new User("pepe@pepe.com", "aa");
		System.out.println(assembler.toResource(user));
		return new ResponseEntity<>(assembler.toResource(user) , HttpStatus.OK);

	}
}
