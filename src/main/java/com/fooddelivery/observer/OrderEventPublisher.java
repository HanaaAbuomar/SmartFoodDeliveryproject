package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The "subject" of the Observer pattern. Services publish order events here and every
 * registered {@link OrderObserver} is informed. A failing observer never stops the others.
 */
public class OrderEventPublisher {
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();

    /**
     * Registers an observer. Null values and duplicates are ignored.
     *
     * @param observer the observer to add
     */
    public void register(OrderObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * @param observer the observer to remove
     */
    public void unregister(OrderObserver observer) {
        observers.remove(observer);
    }

    /** @return number of registered observers */
    public int observerCount() {
        return observers.size();
    }

    /**
     * Tells all observers that an order changed state.
     *
     * @param order     the updated order
     * @param oldStatus the previous state
     * @param newStatus the new state
     */
    public void publishStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        for (OrderObserver observer : observers) {
            try {
                observer.onStatusChanged(order, oldStatus, newStatus);
            } catch (RuntimeException e) {
                System.err.println("Order observer failed: " + e.getMessage());
            }
        }
    }

    /**
     * Tells all observers that a driver was assigned to an order.
     *
     * @param order  the updated order
     * @param driver the assigned driver
     */
    public void publishDriverAssigned(Order order, Driver driver) {
        for (OrderObserver observer : observers) {
            try {
                observer.onDriverAssigned(order, driver);
            } catch (RuntimeException e) {
                System.err.println("Order observer failed: " + e.getMessage());
            }
        }
    }
}
