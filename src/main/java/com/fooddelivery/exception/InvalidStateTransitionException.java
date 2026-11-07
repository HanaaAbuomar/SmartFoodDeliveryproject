package com.fooddelivery.exception;

import com.fooddelivery.domain.OrderStatus;

/** Thrown when an order is moved to a state that is not allowed from its current state. */
public class InvalidStateTransitionException extends InvalidOrderException {
    private static final long serialVersionUID = 1L;

    /**
     * @param from the current state
     * @param to   the requested state
     */
    public InvalidStateTransitionException(OrderStatus from, OrderStatus to) {
        super(from.isTerminal()
                ? "Order is " + from + " and can no longer be modified"
                : "Invalid status transition: " + from + " -> " + to);
    }
}
