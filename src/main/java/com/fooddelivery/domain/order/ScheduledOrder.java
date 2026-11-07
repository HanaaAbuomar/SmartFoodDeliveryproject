package com.fooddelivery.domain.order;

import java.time.LocalDateTime;

public class ScheduledOrder {
    private String orderId;
    private LocalDateTime scheduledTime;
    private String orderType; // "STANDARD", "EXPRESS", "SCHEDULED"

    public ScheduledOrder(String orderId, LocalDateTime scheduledTime, String orderType) {
        this.orderId = orderId;
        this.orderType = orderType;
        setScheduledTime(scheduledTime);
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        // التأكد من أن وقت الجدولة ليس في الماضي مقارنة بالوقت الحالي
        if (scheduledTime != null && scheduledTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time cannot be in the past");
        }
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrderType() {
        return orderType;
    }
}