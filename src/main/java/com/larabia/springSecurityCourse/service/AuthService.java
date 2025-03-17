package com.larabia.springSecurityCourse.service;

import com.larabia.springSecurityCourse.controller.models.AuthResponse;
import com.larabia.springSecurityCourse.controller.models.AuthenticationRequest;
import com.larabia.springSecurityCourse.controller.models.RegisterRequest;

public interface AuthService {
	
	AuthResponse register(RegisterRequest registerRequest);
	AuthResponse autenticate(AuthenticationRequest autenticationRequest);
}
