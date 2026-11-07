package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;
import java.util.EnumSet;
import java.util.Set;

/**
 * Tells the restaurant when an order is confirmed (new work), cancelled, picked up by the
 * driver, and which driver is coming to collect it.
 */
public class RestaurantNotificationObserver implements OrderObserver {
    private static final Set<OrderStatus> INTERESTING =
            EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED, OrderStatus.OUT_FOR_DELIVERY);

    private final NotificationService notifications;

    /**
     * @param notifications the service used to deliver messages
     */
    public RestaurantNotificationObserver(NotificationService notifications) {
        this.notifications = notifications;
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (INTERESTING.contains(newStatus)) {
            notifications.send(order.getRestaurantId(),
                    "Order " + order.getOrderId() + " " + newStatus.getDescription());
        }
    }

    @Override
    public void onDriverAssigned(Order order, Driver driver) {
        notifications.send(order.getRestaurantId(),
                "Driver " + driver.getName() + " will collect order " + order.getOrderId());
    }
}
