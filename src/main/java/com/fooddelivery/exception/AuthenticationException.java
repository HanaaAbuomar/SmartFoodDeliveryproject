package com.fooddelivery.exception;

/** Thrown when login credentials are invalid. */
public class AuthenticationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message the error message shown to the user
     */
    public AuthenticationException(String message) {
        super(message);
    }
}
