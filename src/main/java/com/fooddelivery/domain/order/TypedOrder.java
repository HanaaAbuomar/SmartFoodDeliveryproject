package com.fooddelivery.domain.order;

import java.time.LocalDateTime;

/**
 * Base class of the supported order types (US5.1).
 *
 * <p>Each type applies its own rules (US5.2) through {@link #getExtraDeliveryFee()} and
 * {@link #validate(LocalDateTime)}, so callers never need to check the type name.
 */
public abstract class TypedOrder {
    private final String orderId;
    private final String orderType;

    /**
     * @param orderId   the id of the order this type applies to
     * @param orderType the type name, e.g. "STANDARD"
     */
    protected TypedOrder(String orderId, String orderType) {
        this.orderId = orderId;
        this.orderType = orderType;
    }

    /**
     * @return the extra amount added to the delivery fee for this type (0 if none)
     */
    public abstract double getExtraDeliveryFee();

    /**
     * Checks the type-specific rules.
     *
     * @param now the current time
     * @throws IllegalArgumentException if the order breaks a rule of its type
     */
    public abstract void validate(LocalDateTime now);

    /**
     * @return the order id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @return the type name, e.g. "EXPRESS"
     */
    public String getOrderType() {
        return orderType;
    }
}
