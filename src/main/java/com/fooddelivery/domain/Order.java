package com.fooddelivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's order in the Smart Food Delivery system.
 */
public class Order {
    private String orderId;
    private String customerId;
    private String restaurantId;
    private List<MenuItem> items;
    private double totalPrice;
    private String status; // e.g., "PENDING", "PREPARING", "DELIVERED", "CANCELLED"

    /**
     * Default constructor initializing an empty items list.
     */
    public Order() {
        this.items = new ArrayList<>();
        this.status = "PENDING";
    }

    /**
     * Constructs a new Order with specified details.
     *
     * @param orderId      the unique identifier of the order
     * @param customerId   the ID of the customer who placed the order
     * @param restaurantId the ID of the restaurant receiving the order
     */
    public Order(String orderId, String customerId, String restaurantId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
        this.status = "PENDING";
    }

    // Business Methods

    /**
     * Adds an item to the order and updates the total price.
     *
     * @param item the MenuItem to add
     */
    public void addItem(MenuItem item) {
        if (item != null) {
            this.items.add(item);
            this.totalPrice += item.getPrice();
        }
    }

    /**
     * Removes an item from the order and updates the total price.
     *
     * @param item the MenuItem to remove
     */
    public void removeItem(MenuItem item) {
        if (this.items.remove(item)) {
            this.totalPrice -= item.getPrice();
        }
    }

    // Getters and Setters

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}