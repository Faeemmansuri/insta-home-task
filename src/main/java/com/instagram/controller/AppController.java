package com.instagram.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instagram.request.LoginRequest;
import com.instagram.request.RegisterRequest;
import com.instagram.service.UserService;

@RestController
public class AppController {
	
	@Autowired UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(userService.loginUser(request));
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> registerUser(@Valid @RequestBody RegisterRequest request) {
		userService.registerUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
