package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;

/**
 * Observer pattern: something that wants to know when an order changes
 * (customer, restaurant and driver notifications, driver release, ...).
 */
public interface OrderObserver {
    /**
     * Called after an order moved to a new state.
     *
     * @param order     the order (already updated)
     * @param oldStatus the previous state
     * @param newStatus the new state
     */
    void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);

    /**
     * Called after a driver was assigned to an order. Does nothing by default.
     *
     * @param order  the order (already updated with the driver id)
     * @param driver the assigned driver
     */
    default void onDriverAssigned(Order order, Driver driver) {
    }
}
