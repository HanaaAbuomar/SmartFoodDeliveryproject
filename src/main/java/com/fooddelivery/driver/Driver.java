package com.fooddelivery.driver;

/**
 * Base class of all delivery drivers (US5.3).
 *
 * <p>Each driver type decides how far it can travel, how many items it can carry and
 * how fast it moves (US5.4). {@link #canDeliver(DeliveryRequest)} applies those limits.
 */
public abstract class Driver {
    private final String id;
    private final String name;
    private double x;
    private double y;
    private DriverStatus status = DriverStatus.AVAILABLE;

    /**
     * @param id   unique driver id
     * @param name driver name
     * @param x    current x coordinate (km)
     * @param y    current y coordinate (km)
     */
    protected Driver(String id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    /** @return the driver type name, e.g. "BICYCLE" */
    public abstract String getType();

    /** @return the longest delivery distance this driver accepts (km) */
    public abstract double getMaxDeliveryDistanceKm();

    /** @return the largest number of item units this driver can carry */
    public abstract int getMaxItems();

    /** @return the average speed (km/h) */
    public abstract double getAverageSpeedKmh();

    /**
     * Checks the type-specific rules (US5.4): distance and order size.
     * Subclasses may add more rules.
     *
     * @param request the delivery to check
     * @return true if this driver is allowed to take the delivery
     */
    public boolean canDeliver(DeliveryRequest request) {
        return request.getDeliveryDistanceKm() <= getMaxDeliveryDistanceKm()
                && request.getItemCount() <= getMaxItems();
    }

    /**
     * @param px x coordinate (km)
     * @param py y coordinate (km)
     * @return straight-line distance from the driver to the point (km)
     */
    public double distanceTo(double px, double py) {
        return Math.hypot(x - px, y - py);
    }

    /**
     * Estimated time to pick the order up and deliver it.
     *
     * @param request the delivery
     * @return estimated minutes
     */
    public double estimatedMinutes(DeliveryRequest request) {
        double km = distanceTo(request.getPickupX(), request.getPickupY())
                + request.getDeliveryDistanceKm();
        return km / getAverageSpeedKmh() * 60.0;
    }

    /** @return true if the driver can take a new order */
    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE;
    }

    /** @return the driver id */
    public String getId() {
        return id;
    }

    /** @return the driver name */
    public String getName() {
        return name;
    }

    /** @return current x coordinate (km) */
    public double getX() {
        return x;
    }

    /** @return current y coordinate (km) */
    public double getY() {
        return y;
    }

    /**
     * Moves the driver.
     *
     * @param x new x coordinate (km)
     * @param y new y coordinate (km)
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /** @return the availability status */
    public DriverStatus getStatus() {
        return status;
    }

    /**
     * @param status the new availability status
     */
    public void setStatus(DriverStatus status) {
        this.status = status;
    }
}
