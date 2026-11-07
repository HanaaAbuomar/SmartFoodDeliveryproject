package com.fooddelivery.service;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.exception.InvalidStateTransitionException;
import com.fooddelivery.observer.OrderEventPublisher;
import com.fooddelivery.repository.OrderRepository;

/**
 * Sprint 4 - moves orders through their lifecycle (US4.1) and informs the observers.
 *
 * <p>Valid flow: PENDING, CONFIRMED, PREPARING, READY, OUT_FOR_DELIVERY, DELIVERED.
 * Invalid transitions are rejected and delivered (or cancelled) orders cannot be modified.
 */
public class OrderLifecycleService {
    private final OrderRepository orders;
    private final OrderEventPublisher publisher;

    /**
     * @param orders    the order store
     * @param publisher where status changes are announced
     */
    public OrderLifecycleService(OrderRepository orders, OrderEventPublisher publisher) {
        this.orders = orders;
        this.publisher = publisher;
    }

    /**
     * Moves an order to a new state.
     *
     * @param orderId the order id
     * @param next    the requested state
     * @return the updated order
     * @throws InvalidOrderException           if the order is missing or the state is null
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    public Order updateStatus(String orderId, OrderStatus next) {
        Order order = orders.findById(orderId);
        if (order == null) {
            throw new InvalidOrderException("Order not found");
        }
        if (next == null) {
            throw new InvalidOrderException("New status is required");
        }
        OrderStatus current = OrderStatus.fromString(order.getStatus());
        if (!current.canTransitionTo(next)) {
            throw new InvalidStateTransitionException(current, next);
        }
        order.setStatus(next.name());
        orders.save(order);
        publisher.publishStatusChanged(order, current, next);
        return order;
    }

    /**
     * @param orderId the order id
     * @return the order after moving to CONFIRMED
     */
    public Order confirm(String orderId) {
        return updateStatus(orderId, OrderStatus.CONFIRMED);
    }

    /**
     * @param orderId the order id
     * @return the order after moving to PREPARING
     */
    public Order startPreparing(String orderId) {
        return updateStatus(orderId, OrderStatus.PREPARING);
    }

    /**
     * @param orderId the order id
     * @return the order after moving to READY
     */
    public Order markReady(String orderId) {
        return updateStatus(orderId, OrderStatus.READY);
    }

    /**
     * @param orderId the order id
     * @return the order after moving to OUT_FOR_DELIVERY
     */
    public Order dispatch(String orderId) {
        return updateStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    /**
     * @param orderId the order id
     * @return the order after moving to DELIVERED
     */
    public Order markDelivered(String orderId) {
        return updateStatus(orderId, OrderStatus.DELIVERED);
    }

    /**
     * Cancels an order. Only PENDING orders can be cancelled (US2.4).
     *
     * @param orderId the order id
     * @return the order after moving to CANCELLED
     */
    public Order cancel(String orderId) {
        return updateStatus(orderId, OrderStatus.CANCELLED);
    }
}
