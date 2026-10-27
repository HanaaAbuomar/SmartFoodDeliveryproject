package com.fooddelivery.repository;

import com.fooddelivery.domain.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of UserRepository.
 * Stores users using their email addresses as keys.
 */
public class InMemoryUserRepository implements UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    /**
     * Saves a user in memory.
     *
     * @param user the user to save
     */
    public void save(User user) {
        if (user == null || user.getEmail() == null) {
            throw new IllegalArgumentException(
                "User and email must not be null"
            );
        }

        users.put(user.getEmail(), user);
    }

    /**
     * Finds a user by email.
     *
     * @param email the email address
     * @return the matching user, or null if not found
     */
    @Override
    public User findByEmail(String email) {
        if (email == null) {
            return null;
        }

        return users.get(email);
    }
}