package com.larabia.springSecurityCourse.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


//Maneja los 401 Unauthorized
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        com.larabia.springSecurityCourse.exeption.ExceptionUtil.writeErrorResponse(
                response,
                HttpStatus.UNAUTHORIZED,
                "No estás autenticado o tu token es inválido o expiró."
        );
        
	 }
}
