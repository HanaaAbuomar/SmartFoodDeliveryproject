package com.fooddelivery.observer;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.Driver;
import com.fooddelivery.driver.DriverStatus;
import com.fooddelivery.repository.DriverRepository;

/** Makes the driver available again when the order is delivered or cancelled. */
public class DriverReleaseObserver implements OrderObserver {
    private final DriverRepository drivers;

    /**
     * @param drivers the driver store
     */
    public DriverReleaseObserver(DriverRepository drivers) {
        this.drivers = drivers;
    }

    @Override
    public void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus) {
        if (!newStatus.isTerminal() || order.getDriverId() == null) {
            return;
        }
        Driver driver = drivers.findById(order.getDriverId());
        if (driver != null) {
            driver.setStatus(DriverStatus.AVAILABLE);
            drivers.save(driver);
        }
    }
}
