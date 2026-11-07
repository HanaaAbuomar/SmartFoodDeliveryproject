package com.fooddelivery.domain.order;

import java.time.LocalDateTime;

/**
 * Standard order: follows the normal delivery policies, with no extra fee and no extra rule.
 */
public class StandardOrder extends TypedOrder {

    /**
     * @param orderId the order id
     */
    public StandardOrder(String orderId) {
        super(orderId, "STANDARD");
    }

    @Override
    public double getExtraDeliveryFee() {
        return 0.0;
    }

    @Override
    public void validate(LocalDateTime now) {
        // Standard orders have no extra rules.
    }
}
