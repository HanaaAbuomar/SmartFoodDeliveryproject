package com.fooddelivery.exception;

/** Thrown when an order violates validation rules or delivery policies. */
public class InvalidOrderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message description of the violated rule
     */
    public InvalidOrderException(String message) {
        super(message);
    }
}
