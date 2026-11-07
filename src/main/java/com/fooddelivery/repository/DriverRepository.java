package com.fooddelivery.repository;

import com.fooddelivery.driver.Driver;
import java.util.List;

/** Storage contract for drivers. */
public interface DriverRepository {
    /**
     * Saves (inserts or updates) a driver.
     *
     * @param driver the driver to store
     */
    void save(Driver driver);

    /**
     * Finds a driver by id.
     *
     * @param id the driver id
     * @return the driver, or {@code null} if absent
     */
    Driver findById(String id);

    /**
     * @return all stored drivers
     */
    List<Driver> findAll();
}
