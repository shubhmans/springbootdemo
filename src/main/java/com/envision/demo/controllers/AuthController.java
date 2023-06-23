package com.envision.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.envision.demo.dtos.security.LoginRequest;
import com.envision.demo.dtos.security.LoginResponse;
import com.envision.demo.dtos.security.RegisterUserRequest;
import com.envision.demo.dtos.security.RegisterUserResponse;
import com.envision.demo.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
		LoginResponse response = authService.login(request);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> resgister(@RequestBody @Valid RegisterUserRequest request) {
    	return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }
}