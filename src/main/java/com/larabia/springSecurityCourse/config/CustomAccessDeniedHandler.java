package com.larabia.springSecurityCourse.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//Maneja errores 403 Forbidden de permisos
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        com.larabia.springSecurityCourse.exeption.ExceptionUtil.writeErrorResponse(
                response,
                HttpStatus.FORBIDDEN,
                "No tenés permisos suficientes para acceder a este recurso."
        );
    }

}
