package com.fooddelivery.driver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class DriverSelectionStrategyTest {
    private final NearestDriverStrategy nearest = new NearestDriverStrategy();
    private final FastestDriverStrategy fastest = new FastestDriverStrategy();

    private DeliveryRequest request(int items, double km) {
        Order o = new Order("O1", "C1", "R1");
        for (int i = 0; i < items; i++) {
            o.addItem(new MenuItem("M1", "Burger", "", 5.0, true));
        }
        return new DeliveryRequest(o, 0, 0, km);
    }

    // ---- nearest ----

    @Test
    void nearestPicksTheClosestDriverToTheRestaurant() {
        Driver far = new CarDriver("D1", "Far", 8, 0);
        Driver close = new CarDriver("D2", "Close", 1, 0);

        Optional<Driver> result = nearest.select(Arrays.<Driver>asList(far, close), request(2, 5));

        assertSame(close, result.get());
    }

    @Test
    void nearestSkipsBusyDrivers() {
        Driver busyClose = new CarDriver("D1", "Busy", 1, 0);
        busyClose.setStatus(DriverStatus.BUSY);
        Driver freeFar = new CarDriver("D2", "Free", 8, 0);

        assertSame(freeFar,
                nearest.select(Arrays.<Driver>asList(busyClose, freeFar), request(2, 5)).get());
    }

    @Test
    void nearestSkipsIneligibleDrivers() {
        Driver bikeClose = new BicycleDriver("D1", "Bike", 1, 0);
        Driver carFar = new CarDriver("D2", "Car", 8, 0);

        // 10 km is too far for the bicycle
        assertSame(carFar,
                nearest.select(Arrays.<Driver>asList(bikeClose, carFar), request(2, 10)).get());
    }

    @Test
    void nearestBreaksTiesById() {
        Driver b = new CarDriver("B", "B", 2, 0);
        Driver a = new CarDriver("A", "A", 0, 2);

        assertSame(a, nearest.select(Arrays.<Driver>asList(b, a), request(2, 5)).get());
    }

    @Test
    void nearestReturnsEmptyWithoutCandidates() {
        assertFalse(nearest.select(Collections.<Driver>emptyList(), request(1, 1)).isPresent());
        assertFalse(nearest.select(null, request(1, 1)).isPresent());
    }

    @Test
    void nearestReturnsEmptyWhenNobodyQualifies() {
        Driver busy = new CarDriver("D1", "Busy", 0, 0);
        busy.setStatus(DriverStatus.BUSY);
        Driver tooSmall = new BicycleDriver("D2", "Bike", 0, 0);

        List<Driver> all = Arrays.<Driver>asList(busy, tooSmall);

        assertTrue(nearest.select(all, request(2, 20)).isEmpty());
    }

    // ---- fastest ----

    @Test
    void fastestPicksTheShortestTotalTime() {
        // bicycle is closer to the restaurant, but the motorcycle is much faster overall
        Driver bike = new BicycleDriver("D1", "Bike", 0.5, 0);
        Driver moto = new MotorcycleDriver("D2", "Moto", 3, 0);

        assertSame(moto, fastest.select(Arrays.<Driver>asList(bike, moto), request(2, 2)).get());
        assertSame(bike, nearest.select(Arrays.<Driver>asList(bike, moto), request(2, 2)).get());
    }

    @Test
    void fastestSkipsBusyAndIneligibleDrivers() {
        Driver busyMoto = new MotorcycleDriver("D1", "Busy", 0, 0);
        busyMoto.setStatus(DriverStatus.BUSY);
        Driver lowBatteryEv = new ElectricVehicleDriver("D2", "EV", 0, 0, 1.0);
        Driver car = new CarDriver("D3", "Car", 5, 0);

        assertSame(car, fastest.select(
                Arrays.<Driver>asList(busyMoto, lowBatteryEv, car), request(2, 8)).get());
    }

    @Test
    void fastestBreaksTiesById() {
        Driver b = new CarDriver("B", "B", 0, 0);
        Driver a = new CarDriver("A", "A", 0, 0);

        assertEquals("A", fastest.select(Arrays.<Driver>asList(b, a), request(1, 3)).get().getId());
    }

    @Test
    void fastestReturnsEmptyWithoutCandidates() {
        assertFalse(fastest.select(Collections.<Driver>emptyList(), request(1, 1)).isPresent());
        assertFalse(fastest.select(null, request(1, 1)).isPresent());
    }
}
