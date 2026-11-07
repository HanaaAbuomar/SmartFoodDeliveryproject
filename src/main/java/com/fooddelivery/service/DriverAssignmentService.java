package com.fooddelivery.service;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.DeliveryRequest;
import com.fooddelivery.driver.Driver;
import com.fooddelivery.driver.DriverSelectionStrategy;
import com.fooddelivery.driver.DriverStatus;
import com.fooddelivery.exception.DriverAssignmentException;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.observer.OrderEventPublisher;
import com.fooddelivery.repository.DriverRepository;
import com.fooddelivery.repository.OrderRepository;
import java.util.EnumSet;
import java.util.Set;

/**
 * Sprint 3 - assigns an available, eligible driver to an order (US3.1, US3.2).
 * The way the driver is chosen is a swappable {@link DriverSelectionStrategy}.
 */
public class DriverAssignmentService {
    private static final Set<OrderStatus> ASSIGNABLE =
            EnumSet.of(OrderStatus.CONFIRMED, OrderStatus.PREPARING, OrderStatus.READY);

    private final OrderRepository orders;
    private final DriverRepository drivers;
    private final OrderEventPublisher publisher;
    private DriverSelectionStrategy strategy;

    /**
     * @param orders    the order store
     * @param drivers   the driver store
     * @param strategy  how drivers are chosen
     * @param publisher where assignments are announced
     */
    public DriverAssignmentService(OrderRepository orders, DriverRepository drivers,
                                   DriverSelectionStrategy strategy,
                                   OrderEventPublisher publisher) {
        this.orders = orders;
        this.drivers = drivers;
        this.strategy = strategy;
        this.publisher = publisher;
    }

    /**
     * Changes the selection strategy at runtime (for example nearest to fastest).
     *
     * @param strategy the new strategy
     * @throws IllegalArgumentException if the strategy is null
     */
    public void setStrategy(DriverSelectionStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy must not be null");
        }
        this.strategy = strategy;
    }

    /**
     * Assigns a driver to an order. The driver becomes BUSY and the order records the driver.
     *
     * @param orderId            the order id
     * @param pickupX            restaurant x coordinate (km)
     * @param pickupY            restaurant y coordinate (km)
     * @param deliveryDistanceKm distance from the restaurant to the customer (km)
     * @return the assigned driver
     * @throws InvalidOrderException     if the order is missing, in the wrong state or
     *                                   already has a driver
     * @throws DriverAssignmentException if no available, eligible driver exists
     */
    public Driver assignDriver(String orderId, double pickupX, double pickupY,
                               double deliveryDistanceKm) {
        Order order = orders.findById(orderId);
        if (order == null) {
            throw new InvalidOrderException("Order not found");
        }
        OrderStatus status = OrderStatus.fromString(order.getStatus());
        if (!ASSIGNABLE.contains(status)) {
            throw new InvalidOrderException(
                    "A driver can only be assigned to confirmed, preparing or ready orders");
        }
        if (order.getDriverId() != null) {
            throw new InvalidOrderException("Order already has a driver");
        }
        DeliveryRequest request =
                new DeliveryRequest(order, pickupX, pickupY, deliveryDistanceKm);
        Driver driver = strategy.select(drivers.findAll(), request)
                .orElseThrow(() -> new DriverAssignmentException(
                        "No available driver for order " + orderId));

        driver.setStatus(DriverStatus.BUSY);
        drivers.save(driver);
        order.setDriverId(driver.getId());
        orders.save(order);
        publisher.publishDriverAssigned(order, driver);
        return driver;
    }
}
