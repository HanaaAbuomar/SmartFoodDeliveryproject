package com.fooddelivery.driver;

/**
 * Base class of all delivery drivers (US5.3).
 *
 * <p>A driver has an identity, a position and an availability status. Each concrete driver
 * type (bicycle, motorcycle, car, electric vehicle) decides how far it can travel, how many
 * items it can carry and how fast it moves (US5.4). {@link #canDeliver(DeliveryRequest)}
 * applies those limits, so driver-selection strategies never need to know the driver type.
 *
 * @author Shahd
 * @version 1.0
 * @see DriverSelectionStrategy
 */
public abstract class Driver {

    /** Unique identifier of the driver. */
    private final String id;

    /** Display name of the driver, used in notification messages. */
    private final String name;

    /** Current x coordinate of the driver, in kilometres. */
    private double x;

    /** Current y coordinate of the driver, in kilometres. */
    private double y;

    /** Availability of the driver; a new driver is {@link DriverStatus#AVAILABLE}. */
    private DriverStatus status = DriverStatus.AVAILABLE;

    /**
     * Creates an available driver at the given position.
     *
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

    /**
     * Gets the driver type.
     *
     * @return the driver type name, e.g. "BICYCLE"
     */
    public abstract String getType();

    /**
     * Gets the longest delivery distance this driver accepts.
     *
     * @return the maximum delivery distance in kilometres
     */
    public abstract double getMaxDeliveryDistanceKm();

    /**
     * Gets the largest order this driver can carry.
     *
     * @return the maximum number of item units
     */
    public abstract int getMaxItems();

    /**
     * Gets the average speed of this driver type.
     *
     * @return the average speed in kilometres per hour
     */
    public abstract double getAverageSpeedKmh();

    /**
     * Checks the type-specific rules (US5.4): delivery distance and order size.
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
     * Calculates the straight-line distance from the driver to a point.
     *
     * @param px x coordinate of the point (km)
     * @param py y coordinate of the point (km)
     * @return the distance from the driver to the point (km)
     */
    public double distanceTo(double px, double py) {
        return Math.hypot(x - px, y - py);
    }

    /**
     * Estimates the time to pick the order up and deliver it, at this driver's average speed.
     *
     * @param request the delivery
     * @return the estimated time in minutes
     */
    public double estimatedMinutes(DeliveryRequest request) {
        double km = distanceTo(request.getPickupX(), request.getPickupY())
                + request.getDeliveryDistanceKm();
        return km / getAverageSpeedKmh() * 60.0;
    }

    /**
     * Tells whether the driver can take a new order.
     *
     * @return true if the status is {@link DriverStatus#AVAILABLE}
     */
    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE;
    }

    /**
     * Gets the driver id.
     *
     * @return the unique driver id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the driver name.
     *
     * @return the driver name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the current x coordinate.
     *
     * @return the x coordinate (km)
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the current y coordinate.
     *
     * @return the y coordinate (km)
     */
    public double getY() {
        return y;
    }

    /**
     * Moves the driver to a new position.
     *
     * @param x new x coordinate (km)
     * @param y new y coordinate (km)
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the availability status.
     *
     * @return the current status
     */
    public DriverStatus getStatus() {
        return status;
    }

    /**
     * Sets the availability status, e.g. BUSY when an order is assigned.
     *
     * @param status the new availability status
     */
    public void setStatus(DriverStatus status) {
        this.status = status;
    }
}
