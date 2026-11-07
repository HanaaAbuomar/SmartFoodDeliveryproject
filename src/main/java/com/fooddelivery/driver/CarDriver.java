package com.fooddelivery.driver;

/** Car driver: long distances and the largest orders (US5.4). */
public class CarDriver extends Driver {
    /** Longest delivery distance for a car (km). */
    public static final double MAX_DISTANCE_KM = 30.0;
    /** Largest order a car can carry (item units). */
    public static final int MAX_ITEMS = 40;
    /** Average car speed in the city (km/h). */
    public static final double SPEED_KMH = 35.0;

    /**
     * @param id   driver id
     * @param name driver name
     * @param x    current x coordinate (km)
     * @param y    current y coordinate (km)
     */
    public CarDriver(String id, String name, double x, double y) {
        super(id, name, x, y);
    }

    @Override
    public String getType() {
        return "CAR";
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
