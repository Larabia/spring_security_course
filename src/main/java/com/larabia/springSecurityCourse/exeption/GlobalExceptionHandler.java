package com.larabia.springSecurityCourse.exeption;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// Usuario no encontrado
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ExceptionUtil.buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    
    // Ya existe un usuario con el email
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return ExceptionUtil.buildResponse(HttpStatus.CONFLICT, ex.getMessage()); // 409 CONFLICT
    }

    // 
    
    // Credenciales inválidas
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ExceptionUtil.buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales inválidas. Verificá el email y la contraseña.");
    }

    
    // Cualquier otro error genérico no manejado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ExceptionUtil.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado.");
    }

    
    // Validaciones de campos registro
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Error de validación");

        return ExceptionUtil.buildResponse(HttpStatus.BAD_REQUEST, mensaje);
    }



}
