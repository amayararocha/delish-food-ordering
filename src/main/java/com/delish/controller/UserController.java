package com.delish.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delish.model.User;
import com.delish.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/profile") //encontra o usuario através do token 
	public ResponseEntity<User> findUserByJwtToken(@RequestHeader("Authorization") String Jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(Jwt);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
