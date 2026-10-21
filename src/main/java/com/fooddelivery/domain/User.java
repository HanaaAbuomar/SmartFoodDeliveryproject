package com.fooddelivery.domain;

/**
 * Represents a user in the Smart Food Delivery system.
 * Serves as a base class for specific user roles.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
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

    /**
     * Gets the unique identifier of the user.
     *
     * @return the user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id the user ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the full name of the user.
     *
     * @param name the user name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email address of the user.
     *
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email the email address to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the authentication password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the authentication password of the user.
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user in the system.
     *
     * @return the user role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user in the system.
     *
     * @param role the user role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
}