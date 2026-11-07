package com.fooddelivery.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import org.junit.jupiter.api.Test;

class DriverTest {

    private DeliveryRequest request(int items, double deliveryKm) {
        return request(items, 0, 0, deliveryKm);
    }

    private DeliveryRequest request(int items, double pickupX, double pickupY, double km) {
        Order o = new Order("O1", "C1", "R1");
        for (int i = 0; i < items; i++) {
            o.addItem(new MenuItem("M1", "Burger", "", 5.0, true));
        }
        return new DeliveryRequest(o, pickupX, pickupY, km);
    }

    // ---- bicycle ----

    @Test
    void bicycleHasALimitedDeliveryDistance() {
        Driver bike = new BicycleDriver("D1", "Bike", 0, 0);

        assertEquals("BICYCLE", bike.getType());
        assertTrue(bike.canDeliver(request(2, 3.0)));
        assertFalse(bike.canDeliver(request(2, 3.1)));
    }

    @Test
    void bicycleCannotCarryLargeOrders() {
        Driver bike = new BicycleDriver("D1", "Bike", 0, 0);

        assertTrue(bike.canDeliver(request(BicycleDriver.MAX_ITEMS, 1)));
        assertFalse(bike.canDeliver(request(BicycleDriver.MAX_ITEMS + 1, 1)));
    }

    // ---- motorcycle ----

    @Test
    void motorcycleSupportsMediumDistances() {
        Driver moto = new MotorcycleDriver("D1", "Moto", 0, 0);

        assertEquals("MOTORCYCLE", moto.getType());
        assertTrue(moto.canDeliver(request(3, 12)));
        assertTrue(moto.canDeliver(request(3, MotorcycleDriver.MAX_DISTANCE_KM)));
        assertFalse(moto.canDeliver(request(3, 16)));
    }

    @Test
    void motorcycleCannotCarryVeryLargeOrders() {
        Driver moto = new MotorcycleDriver("D1", "Moto", 0, 0);

        assertFalse(moto.canDeliver(request(MotorcycleDriver.MAX_ITEMS + 1, 2)));
    }

    // ---- car ----

    @Test
    void carSupportsLargerOrdersThanOtherTypes() {
        Driver car = new CarDriver("D1", "Car", 0, 0);

        assertEquals("CAR", car.getType());
        assertTrue(car.canDeliver(request(30, 20)));
        assertFalse(new MotorcycleDriver("D2", "Moto", 0, 0).canDeliver(request(30, 5)));
        assertFalse(new BicycleDriver("D3", "Bike", 0, 0).canDeliver(request(30, 1)));
    }

    @Test
    void carStillHasALimit() {
        Driver car = new CarDriver("D1", "Car", 0, 0);

        assertFalse(car.canDeliver(request(CarDriver.MAX_ITEMS + 1, 5)));
        assertFalse(car.canDeliver(request(5, CarDriver.MAX_DISTANCE_KM + 1)));
    }

    // ---- electric vehicle ----

    @Test
    void electricVehicleNeedsEnoughBatteryForPickupAndDelivery() {
        // driver is 4 km from the restaurant, delivery is 6 km: needs 10 km of range
        Driver ev = new ElectricVehicleDriver("D1", "EV", 4, 0, 10.0);

        assertEquals("ELECTRIC_VEHICLE", ev.getType());
        assertTrue(ev.canDeliver(request(3, 0, 0, 6)));
        assertFalse(ev.canDeliver(request(3, 0, 0, 6.5)));
    }

    @Test
    void electricVehicleWithLowBatteryIsRejectedEvenForShortTrips() {
        ElectricVehicleDriver ev = new ElectricVehicleDriver("D1", "EV", 0, 0, 1.0);

        assertFalse(ev.canDeliver(request(1, 5)));

        ev.setBatteryRangeKm(20.0);
        assertEquals(20.0, ev.getBatteryRangeKm(), 0.0001);
        assertTrue(ev.canDeliver(request(1, 5)));
    }

    @Test
    void electricVehicleStillObeysNormalLimits() {
        Driver ev = new ElectricVehicleDriver("D1", "EV", 0, 0, 1000.0);

        assertFalse(ev.canDeliver(request(ElectricVehicleDriver.MAX_ITEMS + 1, 5)));
        assertFalse(ev.canDeliver(request(1, ElectricVehicleDriver.MAX_DISTANCE_KM + 1)));
    }

    // ---- common behaviour ----

    @Test
    void newDriverIsAvailableAndCanBecomeBusy() {
        Driver d = new CarDriver("D1", "Car", 1, 2);

        assertTrue(d.isAvailable());
        assertEquals("D1", d.getId());
        assertEquals("Car", d.getName());
        assertEquals(DriverStatus.AVAILABLE, d.getStatus());

        d.setStatus(DriverStatus.BUSY);
        assertFalse(d.isAvailable());
    }

    @Test
    void distanceIsStraightLine() {
        Driver d = new CarDriver("D1", "Car", 0, 0);

        assertEquals(5.0, d.distanceTo(3, 4), 0.0001);

        d.setLocation(3, 4);
        assertEquals(3.0, d.getX(), 0.0001);
        assertEquals(4.0, d.getY(), 0.0001);
        assertEquals(0.0, d.distanceTo(3, 4), 0.0001);
    }

    @Test
    void estimatedTimeUsesDriverSpeed() {
        // 3 km to the restaurant + 7 km delivery = 10 km
        DeliveryRequest r = request(1, 3, 0, 7);
        Driver bike = new BicycleDriver("D1", "Bike", 0, 0);
        Driver moto = new MotorcycleDriver("D2", "Moto", 0, 0);

        assertEquals(10.0 / BicycleDriver.SPEED_KMH * 60.0, bike.estimatedMinutes(r), 0.0001);
        assertEquals(15.0, moto.estimatedMinutes(r), 0.0001);
    }

    @Test
    void everyTypeExposesItsLimits() {
        Driver[] all = {
            new BicycleDriver("a", "a", 0, 0), new MotorcycleDriver("b", "b", 0, 0),
            new CarDriver("c", "c", 0, 0), new ElectricVehicleDriver("d", "d", 0, 0, 5)
        };
        for (Driver d : all) {
            assertTrue(d.getMaxDeliveryDistanceKm() > 0);
            assertTrue(d.getMaxItems() > 0);
            assertTrue(d.getAverageSpeedKmh() > 0);
        }
    }

    // ---- DeliveryRequest ----

    @Test
    void deliveryRequestExposesItsData() {
        DeliveryRequest r = request(3, 1.5, 2.5, 4.0);

        assertEquals(3, r.getItemCount());
        assertEquals(1.5, r.getPickupX(), 0.0001);
        assertEquals(2.5, r.getPickupY(), 0.0001);
        assertEquals(4.0, r.getDeliveryDistanceKm(), 0.0001);
        assertEquals("O1", r.getOrder().getOrderId());
    }

    @Test
    void deliveryRequestRejectsBadInput() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest(null, 0, 0, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new DeliveryRequest(new Order(), 0, 0, -1));
    }
}
