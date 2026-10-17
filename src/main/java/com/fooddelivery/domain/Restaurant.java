package com.fooddelivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the Smart Food Delivery system.
 * Contains restaurant details, status, and its menu items.
 */
public class Restaurant {
    private String id;
    private String name;
    private String address;
    private boolean isOpen;
    private List<MenuItem> menu;

    /**
     * Default constructor initializing an empty menu list.
     */
    public Restaurant() {
        this.menu = new ArrayList<>();
    }

    /**
     * Constructs a new Restaurant with specified details.
     *
     * @param id      the unique identifier of the restaurant
     * @param name    the name of the restaurant
     * @param address the physical address of the restaurant
     * @param isOpen  the operational status of the restaurant
     */
    public Restaurant(String id, String name, String address, boolean isOpen) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isOpen = isOpen;
        this.menu = new ArrayList<>();
    }

    // Business Methods

    /**
     * Adds a menu item to the restaurant's menu.
     *
     * @param item the MenuItem to add
     */
    public void addMenuItem(MenuItem item) {
        if (item != null) {
            this.menu.add(item);
        }
    }

    /**
     * Removes a menu item from the restaurant's menu.
     *
     * @param item the MenuItem to remove
     */
    public void removeMenuItem(MenuItem item) {
        this.menu.remove(item);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }
}