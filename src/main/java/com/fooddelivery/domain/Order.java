package com.fooddelivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer's order in the Smart Food Delivery system.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
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

    /**
     * Gets the unique identifier of the order.
     *
     * @return the order ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the unique identifier of the order.
     *
     * @param orderId the order ID to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * Gets the customer ID associated with the order.
     *
     * @return the customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID associated with destination order.
     *
     * @param customerId the customer ID to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the restaurant ID receiving the order.
     *
     * @return the restaurant ID
     */
    public String getRestaurantId() {
        return restaurantId;
    }

    /**
     * Sets the restaurant ID receiving the order.
     *
     * @param restaurantId the restaurant ID to set
     */
    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    /**
     * Gets the list of menu items included in the order.
     *
     * @return the list of items
     */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * Sets the list of menu items for the order.
     *
     * @param items the list of items to set
     */
    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    /**
     * Gets the total price of the order.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }

    /**
     * Sets the total price of the order.
     *
     * @param totalPrice the total price to set
     */
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Gets the current status of the order.
     *
     * @return the order status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of the order.
     *
     * @param status the order status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}