package com.fooddelivery.domain;

/**
 * Represents a menu item available in a restaurant.
 */
public class MenuItem {
    private String id;
    private String name;
    private String description;
    private double price;
    private boolean isAvailable;

    /**
     * Default constructor for creating an uninitialized MenuItem.
     */
    public MenuItem() {
    }

    /**
     * Constructs a new MenuItem with specified details.
     *
     * @param id          the unique identifier of the menu item
     * @param name        the name of the item
     * @param description a brief description of the item
     * @param price       the price of the item
     * @param isAvailable the availability status of the item
     */
    public MenuItem(String id, String name, String description, double price, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}