package com.fooddelivery.driver;

/**
 * Electric vehicle driver. On top of the normal limits it needs enough battery range
 * to reach the restaurant and then deliver the order (US5.4).
 */
public class ElectricVehicleDriver extends Driver {
    /** Longest delivery distance for an electric vehicle (km). */
    public static final double MAX_DISTANCE_KM = 25.0;
    /** Largest order an electric vehicle can carry (item units). */
    public static final int MAX_ITEMS = 25;
    /** Average electric vehicle speed (km/h). */
    public static final double SPEED_KMH = 35.0;

    private double batteryRangeKm;

    /**
     * @param id             driver id
     * @param name           driver name
     * @param x              current x coordinate (km)
     * @param y              current y coordinate (km)
     * @param batteryRangeKm distance the vehicle can still travel (km)
     */
    public ElectricVehicleDriver(String id, String name, double x, double y,
                                 double batteryRangeKm) {
        super(id, name, x, y);
        this.batteryRangeKm = batteryRangeKm;
    }

    @Override
    public String getType() {
        return "ELECTRIC_VEHICLE";
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

    /**
     * Applies the normal limits and also requires that the remaining battery range
     * covers the trip to the restaurant plus the delivery.
     *
     * @param request the delivery to check
     * @return true if the vehicle can take the delivery
     */
    @Override
    public boolean canDeliver(DeliveryRequest request) {
        double needed = distanceTo(request.getPickupX(), request.getPickupY())
                + request.getDeliveryDistanceKm();
        return super.canDeliver(request) && batteryRangeKm >= needed;
    }

    /** @return remaining battery range (km) */
    public double getBatteryRangeKm() {
        return batteryRangeKm;
    }

    /**
     * @param batteryRangeKm new remaining battery range (km)
     */
    public void setBatteryRangeKm(double batteryRangeKm) {
        this.batteryRangeKm = batteryRangeKm;
    }
}
