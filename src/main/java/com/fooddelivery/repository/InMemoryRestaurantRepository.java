package com.fooddelivery.repository;

import com.fooddelivery.domain.Restaurant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of RestaurantRepository.
 * Stores restaurants using their IDs as keys.
 */
public class InMemoryRestaurantRepository implements RestaurantRepository {

    private final Map<String, Restaurant> restaurants =
            new ConcurrentHashMap<>();

    /**
     * Saves or updates a restaurant in memory.
     *
     * @param restaurant the restaurant to store
     */
    public void save(Restaurant restaurant) {
        if (restaurant == null || restaurant.getId() == null) {
            throw new IllegalArgumentException(
                    "Restaurant and restaurant ID must not be null");
        }

        restaurants.put(restaurant.getId(), restaurant);
    }

    /**
     * Finds a restaurant by its ID.
     *
     * @param id the restaurant ID
     * @return the restaurant, or null if not found
     */
    @Override
    public Restaurant findById(String id) {
        if (id == null) {
            return null;
        }

        return restaurants.get(id);
    }

    /**
     * Returns all restaurants stored in memory.
     *
     * @return a list of all stored restaurants
     */
    @Override
    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants.values());
    }
}