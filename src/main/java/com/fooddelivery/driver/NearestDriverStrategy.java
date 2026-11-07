package com.fooddelivery.driver;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/** Picks the eligible driver who is closest to the restaurant. Ties are broken by driver id. */
public class NearestDriverStrategy implements DriverSelectionStrategy {

    @Override
    public Optional<Driver> select(List<Driver> candidates, DeliveryRequest request) {
        if (candidates == null) {
            return Optional.empty();
        }
        return candidates.stream()
                .filter(d -> d.isAvailable() && d.canDeliver(request))
                .min(Comparator
                        .comparingDouble((Driver d) ->
                                d.distanceTo(request.getPickupX(), request.getPickupY()))
                        .thenComparing(Driver::getId));
    }
}
