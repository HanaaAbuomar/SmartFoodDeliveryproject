package com.fooddelivery.domain.order;

import java.time.LocalDateTime;

/**
 * Express order: requires an additional delivery fee (US5.2).
 */
public class ExpressOrder extends TypedOrder {
    /** Default extra delivery fee for express orders. */
    public static final double DEFAULT_EXTRA_FEE = 10.0;

    private final double extraFee;

    /**
     * Creates an express order with the default extra fee.
     *
     * @param orderId the order id
     */
    public ExpressOrder(String orderId) {
        this(orderId, DEFAULT_EXTRA_FEE);
    }

    /**
     * @param orderId  the order id
     * @param extraFee the extra delivery fee
     * @throws IllegalArgumentException if the fee is negative
     */
    public ExpressOrder(String orderId, double extraFee) {
        super(orderId, "EXPRESS");
        if (extraFee < 0) {
            throw new IllegalArgumentException("Extra fee cannot be negative");
        }
        this.extraFee = extraFee;
    }

    @Override
    public double getExtraDeliveryFee() {
        return extraFee;
    }

    @Override
    public void validate(LocalDateTime now) {
        // Express orders only differ by their extra fee.
    }
}
