package com.fooddelivery.driver;

/** Motorcycle driver: fast, medium-distance deliveries (US5.4). */
public class MotorcycleDriver extends Driver {
    /** Longest delivery distance for a motorcycle (km). */
    public static final double MAX_DISTANCE_KM = 15.0;
    /** Largest order a motorcycle can carry (item units). */
    public static final int MAX_ITEMS = 10;
    /** Average motorcycle speed (km/h). */
    public static final double SPEED_KMH = 40.0;

    /**
     * @param id   driver id
     * @param name driver name
     * @param x    current x coordinate (km)
     * @param y    current y coordinate (km)
     */
    public MotorcycleDriver(String id, String name, double x, double y) {
        super(id, name, x, y);
    }

    @Override
    public String getType() {
        return "MOTORCYCLE";
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
