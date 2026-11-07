package com.fooddelivery.repository;

import com.fooddelivery.driver.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** In-memory implementation of {@link DriverRepository}, keyed by driver id. */
public class InMemoryDriverRepository implements DriverRepository {

    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();

    /**
     * Saves or updates a driver.
     *
     * @param driver the driver to store
     * @throws IllegalArgumentException if the driver or its id is null
     */
    @Override
    public void save(Driver driver) {
        if (driver == null || driver.getId() == null) {
            throw new IllegalArgumentException("Driver and driver ID must not be null");
        }
        drivers.put(driver.getId(), driver);
    }

    /**
     * @param id the driver id
     * @return the driver, or null if not found
     */
    @Override
    public Driver findById(String id) {
        if (id == null) {
            return null;
        }
        return drivers.get(id);
    }

    /**
     * @return a copy of the list of all drivers
     */
    @Override
    public List<Driver> findAll() {
        return new ArrayList<>(drivers.values());
    }
}
