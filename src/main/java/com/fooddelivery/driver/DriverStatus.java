package com.fooddelivery.driver;

/** Availability of a driver (US3.1). */
public enum DriverStatus {
    /** Free to take a new order. */
    AVAILABLE,
    /** Currently delivering an order. */
    BUSY
}
