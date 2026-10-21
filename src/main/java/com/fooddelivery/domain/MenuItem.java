package com.fooddelivery.domain;

/**
 * Represents a menu item available in a restaurant.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
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

    /**
     * Gets the unique identifier of the menu item.
     *
     * @return the item ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the menu item.
     *
     * @param id the item ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the name of the menu item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item.
     *
     * @param name the item name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the description of the menu item.
     *
     * @return the item description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the menu item.
     *
     * @param description the item description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the price of the menu item.
     *
     * @return the item price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     *
     * @param price the item price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Checks if the menu item is currently available.
     *
     * @return true if the item is available, false otherwise
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the menu item.
     *
     * @param available the availability status to set
     */
    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}