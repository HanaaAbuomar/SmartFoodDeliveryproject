package com.fooddelivery.domain;

import com.fooddelivery.exception.InvalidOrderException;
import java.util.EnumSet;
import java.util.Set;

/**
 * Lifecycle states of an order (US4.1).
 *
 * <p>Allowed flow:
 * {@code PENDING -> CONFIRMED -> PREPARING -> READY -> OUT_FOR_DELIVERY -> DELIVERED}.
 * An order can be {@code CANCELLED} only while it is still {@code PENDING} (US2.4).
 * {@code DELIVERED} and {@code CANCELLED} are final: nothing can follow them.
 */
public enum OrderStatus {
    /** Order created, waiting for the restaurant. */
    PENDING("is pending"),
    /** Restaurant accepted the order. */
    CONFIRMED("has been confirmed"),
    /** Restaurant is preparing the food. */
    PREPARING("is being prepared"),
    /** Food is ready for pickup. */
    READY("is ready for pickup"),
    /** Driver picked the order up. */
    OUT_FOR_DELIVERY("is out for delivery"),
    /** Order reached the customer. */
    DELIVERED("has been delivered"),
    /** Order was cancelled before preparation. */
    CANCELLED("has been cancelled");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    /**
     * @return a short phrase describing the state, used in notification messages
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the set of states this state may legally move to (empty for final states)
     */
    public Set<OrderStatus> allowedNext() {
        switch (this) {
            case PENDING:
                return EnumSet.of(CONFIRMED, CANCELLED);
            case CONFIRMED:
                return EnumSet.of(PREPARING);
            case PREPARING:
                return EnumSet.of(READY);
            case READY:
                return EnumSet.of(OUT_FOR_DELIVERY);
            case OUT_FOR_DELIVERY:
                return EnumSet.of(DELIVERED);
            default:
                return EnumSet.noneOf(OrderStatus.class);
        }
    }

    /**
     * @param next the requested new state
     * @return true if moving from this state to {@code next} is allowed
     */
    public boolean canTransitionTo(OrderStatus next) {
        return next != null && allowedNext().contains(next);
    }

    /**
     * @return true for {@code DELIVERED} and {@code CANCELLED}
     */
    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * Converts the text stored in {@link Order#getStatus()} to an enum value.
     * Case, spaces and hyphens are ignored, so "Out for delivery" works.
     *
     * @param text the status text
     * @return the matching state
     * @throws InvalidOrderException if the text is missing or unknown
     */
    public static OrderStatus fromString(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new InvalidOrderException("Order status is missing");
        }
        String normalized = text.trim().toUpperCase().replace(' ', '_').replace('-', '_');
        try {
            return OrderStatus.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new InvalidOrderException("Unknown order status: " + text);
        }
    }
}
