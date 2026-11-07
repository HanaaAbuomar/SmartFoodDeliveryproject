package com.fooddelivery.driver;

/** Bicycle driver: slow, small orders and a limited delivery distance (US5.4). */
public class BicycleDriver extends Driver {
    /** Longest delivery distance for a bicycle (km). */
    public static final double MAX_DISTANCE_KM = 3.0;
    /** Largest order a bicycle can carry (item units). */
    public static final int MAX_ITEMS = 5;
    /** Average bicycle speed (km/h). */
    public static final double SPEED_KMH = 15.0;

    /**
     * @param id   driver id
     * @param name driver name
     * @param x    current x coordinate (km)
     * @param y    current y coordinate (km)
     */
    public BicycleDriver(String id, String name, double x, double y) {
        super(id, name, x, y);
    }

    @Override
    public String getType() {
        return "BICYCLE";
    }

    @Override
    public double getMaxDeliveryDistanceKm() {
        return MAX_DISTANCE_KM;
    }

    @Override
    public int getMaxItems() {
        return MAX_ITEMS;
    }

    @Override
    public double getAverageSpeedKmh() {
        return SPEED_KMH;
    }
}
