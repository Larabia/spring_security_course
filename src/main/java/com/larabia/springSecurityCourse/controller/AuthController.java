package com.larabia.springSecurityCourse.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.larabia.springSecurityCourse.controller.models.AuthResponse;
import com.larabia.springSecurityCourse.controller.models.AuthenticationRequest;
import com.larabia.springSecurityCourse.controller.models.RegisterRequest;
import com.larabia.springSecurityCourse.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
		
		return ResponseEntity.ok(authService.register(request));
		
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> autenticate(@Valid @RequestBody AuthenticationRequest request){
		
		return ResponseEntity.ok(authService.autenticate(request));
		
	}

}
