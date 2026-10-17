package com.fooddelivery.domain;

/**
 * Represents a user in the Smart Food Delivery system.
 * Serves as a base class for specific user roles.
 */
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // e.g., "CUSTOMER", "MANAGER"

    /**
     * Default constructor for creating an uninitialized user.
     */
    public User() {
    }

    /**
     * Constructs a new User with specified details.
     *
     * @param id       the unique identifier of the user
     * @param name     the full name of the user
     * @param email    the email address of the user
     * @param password the password for authentication
     * @param role     the role of the user in the system
     */
    public User(String id, String name, String email, String password, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}