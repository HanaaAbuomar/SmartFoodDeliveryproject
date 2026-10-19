package com.fooddelivery.service;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.Restaurant;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import java.util.Map;
import java.util.UUID;

/** Sprint 2 - placing, validating and cancelling orders. */
public class OrderService {
    /** Status of a newly created order. */
    public static final String PENDING = "PENDING";
    /** Status of a cancelled order. */
    public static final String CANCELLED = "CANCELLED";

    private final OrderRepository orders;
    private final RestaurantRepository restaurants;
    private final DeliveryPolicy policy;

    /**
     * @param orders      the order store
     * @param restaurants the restaurant store
     * @param policy      the delivery policy to enforce
     */
    public OrderService(OrderRepository orders, RestaurantRepository restaurants,
                        DeliveryPolicy policy) {
        this.orders = orders;
        this.restaurants = restaurants;
        this.policy = policy;
    }

    /**
     * Places an order (US2.1 - US2.3). A quantity of N adds the item N times.
     *
     * @param customerId      the customer id
     * @param restaurantId    the restaurant id
     * @param itemQuantities  map of menu item id to quantity
     * @param deliveryAddress the delivery address
     * @param distanceKm      distance from the restaurant to the address
     * @return the saved order with status PENDING
     * @throws InvalidOrderException if any rule or policy is violated
     */
    public Order placeOrder(String customerId, String restaurantId,
                            Map<String, Integer> itemQuantities,
                            String deliveryAddress, double distanceKm) {
        Restaurant restaurant = restaurants.findById(restaurantId);
        if (restaurant == null) {
            throw new InvalidOrderException("Restaurant not found");
        }
        if (!restaurant.isOpen()) {
            throw new InvalidOrderException("Restaurant is closed");
        }
        if (itemQuantities == null || itemQuantities.isEmpty()) {
            throw new InvalidOrderException("Order cannot be empty");
        }
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            throw new InvalidOrderException("Invalid delivery address");
        }
        if (distanceKm < 0 || distanceKm > policy.getMaxDistanceKm()) {
            throw new InvalidOrderException("Address is outside the delivery zone");
        }

        Order order = new Order(UUID.randomUUID().toString(), customerId, restaurantId);
        for (Map.Entry<String, Integer> e : itemQuantities.entrySet()) {
            int qty = e.getValue() == null ? 0 : e.getValue();
            if (qty <= 0) {
                throw new InvalidOrderException("Invalid quantity for item " + e.getKey());
            }
            MenuItem item = findItem(restaurant, e.getKey());
            if (item == null) {
                throw new InvalidOrderException("Item not on menu: " + e.getKey());
            }
            if (!item.isAvailable()) {
                throw new InvalidOrderException("Item unavailable: " + item.getName());
            }
            for (int i = 0; i < qty; i++) {
                order.addItem(item);
            }
        }
        if (order.getTotalPrice() < policy.getMinOrderValue()) {
            throw new InvalidOrderException("Order is below the minimum order value");
        }
        order.setStatus(PENDING);
        orders.save(order);
        return order;
    }

    /**
     * Cancels a pending order (US2.4).
     *
     * @param orderId the order id
     * @return the cancelled order
     * @throws InvalidOrderException if the order is missing or no longer PENDING
     */
    public Order cancelOrder(String orderId) {
        Order order = orders.findById(orderId);
        if (order == null) {
            throw new InvalidOrderException("Order not found");
        }
        if (!PENDING.equals(order.getStatus())) {
            throw new InvalidOrderException("Only pending orders can be cancelled");
        }
        order.setStatus(CANCELLED);
        orders.save(order);
        return order;
    }

    private MenuItem findItem(Restaurant restaurant, String itemId) {
        for (MenuItem m : restaurant.getMenu()) {
            if (m.getId() != null && m.getId().equals(itemId)) {
                return m;
            }
        }
        return null;
    }
}
