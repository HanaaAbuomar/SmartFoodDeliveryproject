package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.exception.InvalidStateTransitionException;
import com.fooddelivery.observer.OrderEventPublisher;
import com.fooddelivery.observer.OrderObserver;
import com.fooddelivery.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderLifecycleServiceTest {
    @Mock
    OrderRepository orders;
    @Mock
    OrderObserver observer;

    private OrderLifecycleService service;

    @BeforeEach
    void setUp() {
        OrderEventPublisher publisher = new OrderEventPublisher();
        publisher.register(observer);
        service = new OrderLifecycleService(orders, publisher);
    }

    private Order orderWithStatus(String status) {
        Order o = new Order("O1", "C1", "R1");
        o.setStatus(status);
        when(orders.findById("O1")).thenReturn(o);
        return o;
    }

    @Test
    void fullLifecycleReachesDelivered() {
        Order o = orderWithStatus("PENDING");

        service.confirm("O1");
        service.startPreparing("O1");
        service.markReady("O1");
        service.dispatch("O1");
        service.markDelivered("O1");

        assertEquals("DELIVERED", o.getStatus());
        verify(orders, times(5)).save(o);
        verify(observer).onStatusChanged(o, OrderStatus.PENDING, OrderStatus.CONFIRMED);
        verify(observer).onStatusChanged(o, OrderStatus.CONFIRMED, OrderStatus.PREPARING);
        verify(observer).onStatusChanged(o, OrderStatus.PREPARING, OrderStatus.READY);
        verify(observer).onStatusChanged(o, OrderStatus.READY, OrderStatus.OUT_FOR_DELIVERY);
        verify(observer).onStatusChanged(o, OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED);
    }

    @Test
    void invalidTransitionIsRejectedAndNothingHappens() {
        Order o = orderWithStatus("PENDING");

        assertThrows(InvalidStateTransitionException.class, () -> service.markReady("O1"));

        assertEquals("PENDING", o.getStatus());
        verify(orders, never()).save(any(Order.class));
        verifyNoInteractions(observer);
    }

    @Test
    void deliveredOrderCannotBeModified() {
        Order o = orderWithStatus("DELIVERED");

        for (OrderStatus target : OrderStatus.values()) {
            InvalidStateTransitionException ex = assertThrows(
                    InvalidStateTransitionException.class,
                    () -> service.updateStatus("O1", target));
            assertTrue(ex.getMessage().contains("DELIVERED"));
        }

        assertEquals("DELIVERED", o.getStatus());
        verify(orders, never()).save(any(Order.class));
        verifyNoInteractions(observer);
    }

    @Test
    void pendingOrderCanBeCancelled() {
        Order o = orderWithStatus("PENDING");

        Order result = service.cancel("O1");

        assertEquals("CANCELLED", result.getStatus());
        verify(orders).save(o);
        verify(observer).onStatusChanged(o, OrderStatus.PENDING, OrderStatus.CANCELLED);
    }

    @Test
    void orderInPreparationCannotBeCancelled() {
        Order o = orderWithStatus("PREPARING");

        assertThrows(InvalidStateTransitionException.class, () -> service.cancel("O1"));

        assertEquals("PREPARING", o.getStatus());
        verify(orders, never()).save(any(Order.class));
    }

    @Test
    void unknownOrderIsRejected() {
        when(orders.findById("none")).thenReturn(null);

        assertThrows(InvalidOrderException.class, () -> service.confirm("none"));
    }

    @Test
    void missingNewStatusIsRejected() {
        orderWithStatus("PENDING");

        assertThrows(InvalidOrderException.class, () -> service.updateStatus("O1", null));
    }

    @Test
    void unknownStoredStatusIsRejected() {
        orderWithStatus("WEIRD");

        assertThrows(InvalidOrderException.class, () -> service.confirm("O1"));
    }

    @Test
    void storedStatusTextIsCaseInsensitive() {
        Order o = orderWithStatus("pending");

        service.confirm("O1");

        assertEquals("CONFIRMED", o.getStatus());
    }
}
