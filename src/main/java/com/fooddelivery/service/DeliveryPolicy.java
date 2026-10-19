package com.fooddelivery.service;

/** Delivery rules applied to every order (US2.3). */
public class DeliveryPolicy {
    private final double maxDistanceKm;
    private final double minOrderValue;

    /**
     * @param maxDistanceKm maximum delivery distance in km (the delivery zone)
     * @param minOrderValue minimum allowed order total
     */
    public DeliveryPolicy(double maxDistanceKm, double minOrderValue) {
        this.maxDistanceKm = maxDistanceKm;
        this.minOrderValue = minOrderValue;
    }

    /**
     * @return maximum delivery distance in km
     */
    public double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    /**
     * @return minimum allowed order total
     */
    public double getMinOrderValue() {
        return minOrderValue;
    }
}
