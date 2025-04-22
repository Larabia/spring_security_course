package com.larabia.springSecurityCourse.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class ExceptionUtil {

	//Construye errores dentro del controlador 
    public static ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                message,
                LocalDateTime.now()
        );
        
        return ResponseEntity.status(status).body(error);
    }
    
    //escribe directamente sobre HttpServletResponse para errores fuera del controlador
    public static void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("""
            {
                "status": %d,
                "message": "%s",
                "timestamp": "%s"
            }
            """.formatted(
                status.value(),
                message,
                LocalDateTime.now()
            ));
    }
}
