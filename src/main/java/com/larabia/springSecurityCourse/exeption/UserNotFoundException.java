package com.larabia.springSecurityCourse.exeption;


/**
 * Excepción lanzada cuando no se encuentra un usuario por email.
 */
public class UserNotFoundException extends RuntimeException {
	
    private static final long serialVersionUID = 1L;

	public UserNotFoundException(String message) {
        super(message);
    }

}
