package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import com.fooddelivery.driver.Driver;
import com.fooddelivery.driver.DriverStatus;
import com.fooddelivery.driver.MotorcycleDriver;
import com.fooddelivery.driver.NearestDriverStrategy;
import com.fooddelivery.observer.CustomerNotificationObserver;
import com.fooddelivery.observer.DriverNotificationObserver;
import com.fooddelivery.observer.DriverReleaseObserver;
import com.fooddelivery.observer.NotificationService;
import com.fooddelivery.observer.OrderEventPublisher;
import com.fooddelivery.observer.RestaurantNotificationObserver;
import com.fooddelivery.repository.InMemoryDriverRepository;
import com.fooddelivery.repository.InMemoryOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** End to end: lifecycle + driver assignment + all observers, with a mocked notification service. */
@ExtendWith(MockitoExtension.class)
class NotificationFlowTest {
    @Mock
    NotificationService notifications;

    @Test
    void everyPartyIsNotifiedDuringAFullDelivery() {
        InMemoryOrderRepository orders = new InMemoryOrderRepository();
        InMemoryDriverRepository drivers = new InMemoryDriverRepository();
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.register(new CustomerNotificationObserver(notifications));
        publisher.register(new RestaurantNotificationObserver(notifications));
        publisher.register(new DriverNotificationObserver(notifications));
        publisher.register(new DriverReleaseObserver(drivers));
        OrderLifecycleService lifecycle = new OrderLifecycleService(orders, publisher);
        DriverAssignmentService assignment = new DriverAssignmentService(
                orders, drivers, new NearestDriverStrategy(), publisher);

        Order order = new Order("O1", "C1", "R1");
        order.addItem(new MenuItem("M1", "Burger", "", 5.0, true));
        orders.save(order);
        Driver driver = new MotorcycleDriver("D1", "Sam", 0, 0);
        drivers.save(driver);

        lifecycle.confirm("O1");
        assignment.assignDriver("O1", 0, 0, 4);
        lifecycle.startPreparing("O1");
        lifecycle.markReady("O1");
        lifecycle.dispatch("O1");
        assertEquals(DriverStatus.BUSY, driver.getStatus());
        lifecycle.markDelivered("O1");

        verify(notifications).send("C1", "Your order O1 has been confirmed");
        verify(notifications).send("C1", "Driver Sam has been assigned to your order O1");
        verify(notifications).send("C1", "Your order O1 is out for delivery");
        verify(notifications).send("C1", "Your order O1 has been delivered");
        verify(notifications).send("R1", "Order O1 has been confirmed");
        verify(notifications).send("R1", "Driver Sam will collect order O1");
        verify(notifications).send("D1", "You have been assigned to order O1");
        verify(notifications).send("D1", "Order O1 is ready for pickup");
        assertEquals("DELIVERED", order.getStatus());
        assertEquals("D1", order.getDriverId());
        assertEquals(DriverStatus.AVAILABLE, driver.getStatus());
    }
}
