package com.fooddelivery.driver;

import java.util.List;
import java.util.Optional;

/**
 * Strategy pattern: how to choose a driver for a delivery (US3.2).
 * Implementations must only consider drivers that are available and eligible
 * ({@link Driver#canDeliver(DeliveryRequest)}).
 */
public interface DriverSelectionStrategy {
    /**
     * @param candidates all known drivers (may include busy or ineligible ones)
     * @param request    the delivery to assign
     * @return the chosen driver, or empty if nobody qualifies
     */
    Optional<Driver> select(List<Driver> candidates, DeliveryRequest request);
}
