package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;
import java.util.EnumSet;
import java.util.Set;

/**
 * Tells the driver about a new assignment, and, once assigned, when the order is ready
 * for pickup or cancelled.
 */
public class DriverNotificationObserver implements OrderObserver {
    private static final Set<OrderStatus> INTERESTING =
            EnumSet.of(OrderStatus.READY, OrderStatus.CANCELLED);

    private final NotificationService notifications;

    /**
     * @param notifications the service used to deliver messages
     */
    public DriverNotificationObserver(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (order.getDriverId() != null && INTERESTING.contains(newStatus)) {
            notifications.send(order.getDriverId(),
                    "Order " + order.getOrderId() + " " + newStatus.getDescription());
        }
    }

    @Override
    public void onDriverAssigned(Order order, Driver driver) {
        notifications.send(driver.getId(),
                "You have been assigned to order " + order.getOrderId());
    }
}
