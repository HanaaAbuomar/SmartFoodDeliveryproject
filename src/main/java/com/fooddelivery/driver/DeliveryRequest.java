package com.fooddelivery.driver;

import com.fooddelivery.domain.Order;

/**
 * Everything a driver-selection strategy needs to know about a delivery:
 * the order, where it is picked up and how far it must travel.
 * Locations are simple (x, y) coordinates in kilometres.
 */
public class DeliveryRequest {
    private final Order order;
    private final double pickupX;
    private final double pickupY;
    private final double deliveryDistanceKm;

    /**
     * @param order              the order to deliver
     * @param pickupX            restaurant x coordinate (km)
     * @param pickupY            restaurant y coordinate (km)
     * @param deliveryDistanceKm distance from the restaurant to the customer (km)
     * @throws IllegalArgumentException if the order is null or the distance is negative
     */
    public DeliveryRequest(Order order, double pickupX, double pickupY, double deliveryDistanceKm) {
        if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (deliveryDistanceKm < 0) {
            throw new IllegalArgumentException("Delivery distance must not be negative");
        }
        this.order = order;
        this.pickupX = pickupX;
        this.pickupY = pickupY;
        this.deliveryDistanceKm = deliveryDistanceKm;
    }

    /** @return the order to deliver */
    public Order getOrder() {
        return order;
    }

    /** @return restaurant x coordinate (km) */
    public double getPickupX() {
        return pickupX;
    }

    /** @return restaurant y coordinate (km) */
    public double getPickupY() {
        return pickupY;
    }

    /** @return distance from the restaurant to the customer (km) */
    public double getDeliveryDistanceKm() {
        return deliveryDistanceKm;
    }

    /** @return number of item units in the order */
    public int getItemCount() {
        return order.getItems().size();
    }
}
