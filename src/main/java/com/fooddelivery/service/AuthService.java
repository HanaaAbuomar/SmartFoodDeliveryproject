package com.fooddelivery.service;

import com.fooddelivery.domain.User;
import com.fooddelivery.exception.AuthenticationException;
import com.fooddelivery.repository.UserRepository;

/** US1.1 - validates credentials for customers and managers. */
public class AuthService {
    /** Error message returned for any invalid credentials. */
    public static final String INVALID_CREDENTIALS = "Invalid email or password";

    private final UserRepository users;

    /**
     * @param users the user store
     */
    public AuthService(UserRepository users) {
        this.users = users;
    }

    /**
     * Logs a user in.
     *
     * @param email    the email
     * @param password the password
     * @return the authenticated user
     * @throws AuthenticationException if the credentials are invalid
     */
    public User login(String email, String password) {
        if (email == null || password == null) {
            throw new AuthenticationException(INVALID_CREDENTIALS);
        }
        User user = users.findByEmail(email);
        if (user == null || !password.equals(user.getPassword())) {
            throw new AuthenticationException(INVALID_CREDENTIALS);
        }
        return user;
    }
}
