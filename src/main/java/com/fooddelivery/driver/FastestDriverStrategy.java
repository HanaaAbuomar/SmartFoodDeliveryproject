package com.fooddelivery.driver;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Picks the eligible driver with the shortest estimated total time
 * (trip to the restaurant plus delivery, at the driver's own speed).
 * Ties are broken by driver id.
 */
public class FastestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Optional<Driver> select(List<Driver> candidates, DeliveryRequest request) {
        if (candidates == null) {
            return Optional.empty();
        }
        return candidates.stream()
                .filter(d -> d.isAvailable() && d.canDeliver(request))
                .min(Comparator
                        .comparingDouble((Driver d) -> d.estimatedMinutes(request))
                        .thenComparing(Driver::getId));
    }
}
