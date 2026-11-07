package com.fooddelivery.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.CarDriver;
import com.fooddelivery.driver.Driver;
import com.fooddelivery.driver.DriverStatus;
import com.fooddelivery.repository.DriverRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationObserversTest {
    @Mock
    NotificationService notifications;
    @Mock
    DriverRepository drivers;

    private Order order(String driverId) {
        Order o = new Order("O1", "C1", "R1");
        o.setDriverId(driverId);
        return o;
    }

    private Driver driver() {
        return new CarDriver("D1", "Sam", 0, 0);
    }

    // ---- customer ----

    @Test
    void customerIsToldWhenOrderIsOutForDelivery() {
        new CustomerNotificationObserver(notifications).onStatusChanged(
                order(null), OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY);

        verify(notifications).send("C1", "Your order O1 is out for delivery");
    }

    @Test
    void customerIsToldWhenOrderIsDelivered() {
        new CustomerNotificationObserver(notifications).onStatusChanged(
                order(null), OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED);

        verify(notifications).send("C1", "Your order O1 has been delivered");
    }

    @Test
    void customerIsToldWhenDriverIsAssigned() {
        new CustomerNotificationObserver(notifications).onDriverAssigned(order("D1"), driver());

        verify(notifications).send("C1", "Driver Sam has been assigned to your order O1");
    }

    // ---- restaurant ----

    @Test
    void restaurantIsToldAboutConfirmedCancelledAndPickedUpOrders() {
        RestaurantNotificationObserver observer =
                new RestaurantNotificationObserver(notifications);
        Order o = order(null);

        observer.onStatusChanged(o, OrderStatus.PENDING, OrderStatus.CONFIRMED);
        observer.onStatusChanged(o, OrderStatus.PENDING, OrderStatus.CANCELLED);
        observer.onStatusChanged(o, OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY);

        verify(notifications).send("R1", "Order O1 has been confirmed");
        verify(notifications).send("R1", "Order O1 has been cancelled");
        verify(notifications).send("R1", "Order O1 is out for delivery");
    }

    @Test
    void restaurantIgnoresOtherStatusChanges() {
        new RestaurantNotificationObserver(notifications).onStatusChanged(
                order(null), OrderStatus.CONFIRMED, OrderStatus.PREPARING);

        verifyNoInteractions(notifications);
    }

    @Test
    void restaurantIsToldWhichDriverIsComing() {
        new RestaurantNotificationObserver(notifications).onDriverAssigned(order("D1"), driver());

        verify(notifications).send("R1", "Driver Sam will collect order O1");
    }

    // ---- driver ----

    @Test
    void driverIsToldAboutNewAssignment() {
        new DriverNotificationObserver(notifications).onDriverAssigned(order("D1"), driver());

        verify(notifications).send("D1", "You have been assigned to order O1");
    }

    @Test
    void assignedDriverIsToldWhenOrderIsReadyOrCancelled() {
        DriverNotificationObserver observer = new DriverNotificationObserver(notifications);
        Order o = order("D1");

        observer.onStatusChanged(o, OrderStatus.PREPARING, OrderStatus.READY);
        observer.onStatusChanged(o, OrderStatus.PENDING, OrderStatus.CANCELLED);

        verify(notifications).send("D1", "Order O1 is ready for pickup");
        verify(notifications).send("D1", "Order O1 has been cancelled");
    }

    @Test
    void driverIsNotToldAboutOtherStatusChanges() {
        new DriverNotificationObserver(notifications).onStatusChanged(
                order("D1"), OrderStatus.CONFIRMED, OrderStatus.PREPARING);

        verifyNoInteractions(notifications);
    }

    @Test
    void nobodyIsToldWhenOrderHasNoDriver() {
        new DriverNotificationObserver(notifications).onStatusChanged(
                order(null), OrderStatus.PREPARING, OrderStatus.READY);

        verifyNoInteractions(notifications);
    }

    // ---- driver release ----

    @Test
    void driverBecomesAvailableAgainWhenOrderIsDeliveredOrCancelled() {
        Driver d = driver();
        d.setStatus(DriverStatus.BUSY);
        when(drivers.findById("D1")).thenReturn(d);
        DriverReleaseObserver observer = new DriverReleaseObserver(drivers);

        observer.onStatusChanged(order("D1"), OrderStatus.OUT_FOR_DELIVERY,
                OrderStatus.DELIVERED);
        assertEquals(DriverStatus.AVAILABLE, d.getStatus());

        d.setStatus(DriverStatus.BUSY);
        observer.onStatusChanged(order("D1"), OrderStatus.PENDING, OrderStatus.CANCELLED);
        assertEquals(DriverStatus.AVAILABLE, d.getStatus());
    }

    @Test
    void driverStaysBusyWhileOrderIsInProgress() {
        Driver d = driver();
        d.setStatus(DriverStatus.BUSY);
        DriverReleaseObserver observer = new DriverReleaseObserver(drivers);

        observer.onStatusChanged(order("D1"), OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY);

        assertEquals(DriverStatus.BUSY, d.getStatus());
        verifyNoInteractions(drivers);
    }

    @Test
    void releaseDoesNothingWithoutADriver() {
        new DriverReleaseObserver(drivers).onStatusChanged(
                order(null), OrderStatus.PENDING, OrderStatus.CANCELLED);

        verifyNoInteractions(drivers);
    }

    @Test
    void releaseIgnoresUnknownDriver() {
        when(drivers.findById("D1")).thenReturn(null);

        new DriverReleaseObserver(drivers).onStatusChanged(
                order("D1"), OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED);

        verify(drivers, never()).save(any(Driver.class));
    }
}
