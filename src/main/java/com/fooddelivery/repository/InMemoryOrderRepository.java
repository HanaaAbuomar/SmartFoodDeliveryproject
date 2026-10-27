
package com.fooddelivery.repository;

import com.fooddelivery.domain.Order;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of OrderRepository.
 * Stores orders using their order IDs as keys.
 */
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    /**
     * Saves or updates an order in memory.
     *
     * @param order the order to store
     */
    @Override
    public void save(Order order) {
        if (order == null || order.getOrderId() == null) {
            throw new IllegalArgumentException(
                "Order and order ID must not be null"
            );
        }

        orders.put(order.getOrderId(), order);
    }

    /**
     * Finds an order by its ID.
     *
     * @param id the order ID
     * @return the order, or null if not found
     */
    @Override
    public Order findById(String id) {
        if (id == null) {
            return null;
        }

        return orders.get(id);
    }
}

