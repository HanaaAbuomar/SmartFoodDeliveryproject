package com.fooddelivery.service;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import java.util.ArrayList;
import java.util.List;

/** US1.2 and US1.3 - restaurant and menu browsing. */
public class RestaurantService {
    private final RestaurantRepository restaurants;

    /**
     * @param restaurants the restaurant store
     */
    public RestaurantService(RestaurantRepository restaurants) {
        this.restaurants = restaurants;
    }

    /**
     * Lists restaurants that are currently open.
     *
     * @return open restaurants only
     */
    public List<Restaurant> getOpenRestaurants() {
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant r : restaurants.findAll()) {
            if (r.isOpen()) {
                result.add(r);
            }
        }
        return result;
    }

    /**
     * Lists the available items of a restaurant menu.
     *
     * @param restaurantId the restaurant id
     * @return available items only
     * @throws IllegalArgumentException if the restaurant does not exist
     */
    public List<MenuItem> getAvailableMenu(String restaurantId) {
        Restaurant r = restaurants.findById(restaurantId);
        if (r == null) {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantId);
        }
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem m : r.getMenu()) {
            if (m.isAvailable()) {
                result.add(m);
            }
        }
        return result;
    }
}
