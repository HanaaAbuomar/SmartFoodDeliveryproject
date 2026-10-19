package com.fooddelivery.repository;

import com.fooddelivery.domain.User;

/** Storage contract for users (implemented by the persistence layer). */
public interface UserRepository {
    /**
     * Finds a user by email.
     *
     * @param email the email
     * @return the user, or {@code null} if absent
     */
    User findByEmail(String email);
}
