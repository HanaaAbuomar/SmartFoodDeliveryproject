package com.fooddelivery.repository;

import com.fooddelivery.domain.Restaurant;
import java.util.List;

/** Storage contract for restaurants (implemented by the persistence layer). */
public interface RestaurantRepository {
    /**
     * Finds a restaurant by id.
     *
     * @param id the restaurant id
     * @return the restaurant, or {@code null} if absent
     */
    Restaurant findById(String id);

    /**
     * Returns all restaurants.
     *
     * @return all stored restaurants
     */
    List<Restaurant> findAll();
}
