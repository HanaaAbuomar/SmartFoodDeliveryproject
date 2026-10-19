package com.fooddelivery.repository;

import com.fooddelivery.domain.Order;

/** Storage contract for orders (implemented by the persistence layer). */
public interface OrderRepository {
    /**
     * Saves (inserts or updates) an order.
     *
     * @param order the order to store
     */
    void save(Order order);

    /**
     * Finds an order by id.
     *
     * @param id the order id
     * @return the order, or {@code null} if absent
     */
    Order findById(String id);
}
