package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import com.fooddelivery.driver.BicycleDriver;
import com.fooddelivery.driver.CarDriver;
import com.fooddelivery.driver.Driver;
import com.fooddelivery.driver.DriverStatus;
import com.fooddelivery.driver.FastestDriverStrategy;
import com.fooddelivery.driver.NearestDriverStrategy;
import com.fooddelivery.exception.DriverAssignmentException;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.observer.OrderEventPublisher;
import com.fooddelivery.observer.OrderObserver;
import com.fooddelivery.repository.DriverRepository;
import com.fooddelivery.repository.OrderRepository;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DriverAssignmentServiceTest {
    @Mock
    OrderRepository orders;
    @Mock
    DriverRepository drivers;
    @Mock
    OrderObserver observer;

    private DriverAssignmentService service;

    @BeforeEach
    void setUp() {
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.register(observer);
        service = new DriverAssignmentService(orders, drivers, new NearestDriverStrategy(),
                publisher);
    }

    private Order orderWithStatus(String status, int items) {
        Order o = new Order("O1", "C1", "R1");
        for (int i = 0; i < items; i++) {
            o.addItem(new MenuItem("M1", "Burger", "", 5.0, true));
        }
        o.setStatus(status);
        when(orders.findById("O1")).thenReturn(o);
        return o;
    }

    @Test
    void assignsAvailableDriverAndMarksItBusy() {
        Order o = orderWithStatus("READY", 3);
        Driver car = new CarDriver("D1", "Sam", 0, 0);
        when(drivers.findAll()).thenReturn(Collections.<Driver>singletonList(car));

        Driver result = service.assignDriver("O1", 0, 0, 5);

        assertSame(car, result);
        assertEquals(DriverStatus.BUSY, car.getStatus());
        assertEquals("D1", o.getDriverId());
        verify(drivers).save(car);
        verify(orders).save(o);
        verify(observer).onDriverAssigned(o, car);
    }

    @Test
    void onlyAvailableDriversCanBeAssigned() {
        orderWithStatus("READY", 2);
        Driver busyCar = new CarDriver("D1", "Busy", 0, 0);
        busyCar.setStatus(DriverStatus.BUSY);
        Driver freeBike = new BicycleDriver("D2", "Free", 1, 0);
        when(drivers.findAll()).thenReturn(Arrays.<Driver>asList(busyCar, freeBike));

        Driver result = service.assignDriver("O1", 0, 0, 2);

        assertSame(freeBike, result);
    }

    @Test
    void assignmentIsRejectedWhenThereAreNoDrivers() {
        Order o = orderWithStatus("READY", 2);
        when(drivers.findAll()).thenReturn(Collections.<Driver>emptyList());

        assertThrows(DriverAssignmentException.class,
                () -> service.assignDriver("O1", 0, 0, 2));

        verify(orders, never()).save(any(Order.class));
        verify(drivers, never()).save(any(Driver.class));
        verifyNoInteractions(observer);
        assertEquals(null, o.getDriverId());
    }

    @Test
    void assignmentIsRejectedWhenAllDriversAreBusy() {
        orderWithStatus("READY", 2);
        Driver busy = new CarDriver("D1", "Busy", 0, 0);
        busy.setStatus(DriverStatus.BUSY);
        when(drivers.findAll()).thenReturn(Collections.<Driver>singletonList(busy));

        assertThrows(DriverAssignmentException.class,
                () -> service.assignDriver("O1", 0, 0, 2));
    }

    @Test
    void assignmentIsRejectedWhenNoDriverIsEligible() {
        orderWithStatus("READY", 2);
        Driver bike = new BicycleDriver("D1", "Bike", 0, 0);
        when(drivers.findAll()).thenReturn(Collections.<Driver>singletonList(bike));

        // 10 km is beyond what a bicycle can do
        assertThrows(DriverAssignmentException.class,
                () -> service.assignDriver("O1", 0, 0, 10));
    }

    @Test
    void nearestStrategyPicksTheClosestDriver() {
        orderWithStatus("READY", 2);
        Driver bike = new BicycleDriver("D1", "Bike", 1, 0);
        Driver car = new CarDriver("D2", "Car", 3, 0);
        when(drivers.findAll()).thenReturn(Arrays.<Driver>asList(car, bike));

        assertSame(bike, service.assignDriver("O1", 0, 0, 2));
    }

    @Test
    void strategyCanBeSwappedAtRuntime() {
        orderWithStatus("READY", 2);
        Driver bike = new BicycleDriver("D1", "Bike", 1, 0);
        Driver car = new CarDriver("D2", "Car", 3, 0);
        when(drivers.findAll()).thenReturn(Arrays.<Driver>asList(car, bike));
        service.setStrategy(new FastestDriverStrategy());

        // the car is farther away but much faster overall
        assertSame(car, service.assignDriver("O1", 0, 0, 2));
    }

    @Test
    void nullStrategyIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> service.setStrategy(null));
    }

    @Test
    void orderMustBeConfirmedPreparingOrReady() {
        Order o = orderWithStatus("PENDING", 1);

        for (String status : new String[] {"PENDING", "OUT_FOR_DELIVERY", "DELIVERED",
            "CANCELLED"}) {
            o.setStatus(status);
            assertThrows(InvalidOrderException.class,
                    () -> service.assignDriver("O1", 0, 0, 2), status);
        }

        verify(drivers, never()).findAll();
    }

    @Test
    void unknownOrderIsRejected() {
        when(orders.findById("none")).thenReturn(null);

        assertThrows(InvalidOrderException.class,
                () -> service.assignDriver("none", 0, 0, 2));
    }

    @Test
    void orderThatAlreadyHasADriverIsRejected() {
        Order o = orderWithStatus("READY", 1);
        o.setDriverId("D9");

        assertThrows(InvalidOrderException.class,
                () -> service.assignDriver("O1", 0, 0, 2));

        verify(drivers, never()).findAll();
    }
}
