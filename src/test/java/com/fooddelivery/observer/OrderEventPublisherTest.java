package com.fooddelivery.observer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.driver.CarDriver;
import com.fooddelivery.driver.Driver;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderEventPublisherTest {
    @Mock
    OrderObserver first;
    @Mock
    OrderObserver second;

    private final OrderEventPublisher publisher = new OrderEventPublisher();
    private final Order order = new Order("O1", "C1", "R1");

    @Test
    void registerIgnoresNullAndDuplicates() {
        publisher.register(first);
        publisher.register(first);
        publisher.register(null);

        assertEquals(1, publisher.observerCount());
    }

    @Test
    void unregisteredObserverIsNoLongerInformed() {
        publisher.register(first);
        publisher.unregister(first);

        publisher.publishStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);

        assertEquals(0, publisher.observerCount());
        verifyNoInteractions(first);
    }

    @Test
    void everyObserverIsInformedOfStatusChanges() {
        publisher.register(first);
        publisher.register(second);

        publisher.publishStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);

        verify(first).onStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);
        verify(second).onStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);
    }

    @Test
    void everyObserverIsInformedOfDriverAssignments() {
        Driver driver = new CarDriver("D1", "Sam", 0, 0);
        publisher.register(first);
        publisher.register(second);

        publisher.publishDriverAssigned(order, driver);

        verify(first).onDriverAssigned(order, driver);
        verify(second).onDriverAssigned(order, driver);
    }

    @Test
    void failingObserverDoesNotStopTheOthers() {
        doThrow(new RuntimeException("boom")).when(first).onStatusChanged(
                any(Order.class), any(OrderStatus.class), any(OrderStatus.class));
        publisher.register(first);
        publisher.register(second);

        publisher.publishStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);

        verify(second).onStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);
    }

    @Test
    void failingObserverDoesNotStopDriverAssignmentNotifications() {
        Driver driver = new CarDriver("D1", "Sam", 0, 0);
        doThrow(new RuntimeException("boom")).when(first).onDriverAssigned(
                any(Order.class), any(Driver.class));
        publisher.register(first);
        publisher.register(second);

        publisher.publishDriverAssigned(order, driver);

        verify(second).onDriverAssigned(order, driver);
    }

    @Test
    void publishingWithoutObserversIsHarmless() {
        publisher.publishStatusChanged(order, OrderStatus.PENDING, OrderStatus.CONFIRMED);
        publisher.publishDriverAssigned(order, new CarDriver("D1", "Sam", 0, 0));

        assertEquals(0, publisher.observerCount());
    }

    @Test
    void consoleNotificationServicePrintsTheMessage() {
        PrintStream original = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            new ConsoleNotificationService().send("C1", "hello");
        } finally {
            System.setOut(original);
        }

        assertTrue(out.toString().contains("[NOTIFY -> C1] hello"));
    }
}
