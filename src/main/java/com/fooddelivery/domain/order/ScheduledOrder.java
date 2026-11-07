package com.fooddelivery.domain.order;

import java.time.LocalDateTime;

/**
 * Scheduled order: must be delivered at a time in the future (US5.2).
 */
public class ScheduledOrder extends TypedOrder {
    private LocalDateTime scheduledTime;

    /**
     * @param orderId       the order id
     * @param scheduledTime the requested delivery time (must not be in the past)
     */
    public ScheduledOrder(String orderId, LocalDateTime scheduledTime) {
        this(orderId, scheduledTime, "SCHEDULED");
    }

    /**
     * @param orderId       the order id
     * @param scheduledTime the requested delivery time (must not be in the past)
     * @param orderType     the type name to report
     */
    public ScheduledOrder(String orderId, LocalDateTime scheduledTime, String orderType) {
        super(orderId, orderType);
        setScheduledTime(scheduledTime);
    }

    /**
     * @param scheduledTime the new delivery time
     * @throws IllegalArgumentException if the time is in the past
     */
    public void setScheduledTime(LocalDateTime scheduledTime) {
        if (scheduledTime != null && scheduledTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time cannot be in the past");
        }
        this.scheduledTime = scheduledTime;
    }

    /**
     * @return the requested delivery time
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    @Override
    public double getExtraDeliveryFee() {
        return 0.0;
    }

    /**
     * @param now the current time
     * @throws IllegalArgumentException if no time is set or it is not after {@code now}
     */
    @Override
    public void validate(LocalDateTime now) {
        if (scheduledTime == null || !scheduledTime.isAfter(now)) {
            throw new IllegalArgumentException("Scheduled order requires a future delivery time");
        }
    }
}
