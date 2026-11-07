package com.fooddelivery.exception;

/** Thrown when no driver can be assigned to an order (US3.2). */
public class DriverAssignmentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * @param message why the assignment failed
     */
    public DriverAssignmentException(String message) {
        super(message);
    }
}
