package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;

/**
 * Tells the customer about every status change and when a driver is assigned (US3.3, US3.4).
 */
public class CustomerNotificationObserver implements OrderObserver {
    private final NotificationService notifications;

    /**
     * @param notifications the service used to deliver messages
     */
    public CustomerNotificationObserver(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        notifications.send(order.getCustomerId(),
                "Your order " + order.getOrderId() + " " + newStatus.getDescription());
    }

    @Override
    public void onDriverAssigned(Order order, Driver driver) {
        notifications.send(order.getCustomerId(),
                "Driver " + driver.getName() + " has been assigned to your order "
                        + order.getOrderId());
    }
}
