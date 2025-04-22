package com.larabia.springSecurityCourse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/greeting")
public class GreetingController {
	
	@GetMapping("/sayHelloPublic")
	public String sayHello(){
		return "Hello from api";
	}
	
	@GetMapping("/sayHelloProtected")
	public String sayHelloProtected(){
		return "Hello from api protected";
	}
	
	@GetMapping("/sayHelloAdmin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> sayHelloAdmin() {
	    return ResponseEntity.ok("Hola Admin 👑");
	}
	
	@GetMapping("/sayHelloModeration")
	@PreAuthorize("hasAnyRole('MODERATOR', 'ADMIN')")
	public String sayHelloModeration() {
	    return "👮 ¡Hola Moderador o Admin!";
	}

}

