package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.Restaurant;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.repository.OrderRepository;
import com.fooddelivery.repository.RestaurantRepository;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    OrderRepository orders;
    @Mock
    RestaurantRepository restaurants;

    private OrderService service;

    @BeforeEach
    void setUp() {
        Restaurant r = new Restaurant("R1", "Grill", "Amman", true);
        r.addMenuItem(new MenuItem("M1", "Burger", "", 5.0, true));
        r.addMenuItem(new MenuItem("M2", "Fries", "", 2.0, false));
        Restaurant closed = new Restaurant("R2", "Closed", "Amman", false);
        closed.addMenuItem(new MenuItem("M9", "Soup", "", 9.0, true));
        lenient().when(restaurants.findById("R1")).thenReturn(r);
        lenient().when(restaurants.findById("R2")).thenReturn(closed);
        // delivery zone: 10 km, minimum order value: 8
        service = new OrderService(orders, restaurants, new DeliveryPolicy(10, 8.0));
    }

    private Map<String, Integer> cart(String id, Integer qty) {
        Map<String, Integer> m = new HashMap<>();
        m.put(id, qty);
        return m;
    }

    private void assertRejected(Map<String, Integer> cart, String address, double km) {
        assertThrows(InvalidOrderException.class,
                () -> service.placeOrder("C1", "R1", cart, address, km));
        verify(orders, never()).save(any(Order.class));
    }

    @Test
    void placeOrderCreatesPendingOrderWithCorrectTotal() {
        Order o = service.placeOrder("C1", "R1", cart("M1", 2), "Street 1", 3);

        assertEquals("PENDING", o.getStatus());
        assertEquals(10.0, o.getTotalPrice(), 0.001);
        assertEquals("C1", o.getCustomerId());
        verify(orders).save(o);
    }

    @Test
    void emptyOrderRejected() {
        assertRejected(Collections.<String, Integer>emptyMap(), "S", 1);
        assertRejected(null, "S", 1);
    }

    @Test
    void unavailableItemRejected() {
        assertRejected(cart("M2", 5), "S", 1);
    }

    @Test
    void itemNotOnMenuRejected() {
        assertRejected(cart("X", 1), "S", 1);
    }

    @Test
    void invalidQuantitiesRejected() {
        assertRejected(cart("M1", 0), "S", 1);
        assertRejected(cart("M1", -3), "S", 1);
        assertRejected(cart("M1", null), "S", 1);
    }

    @Test
    void outsideDeliveryZoneRejected() {
        assertRejected(cart("M1", 2), "S", 25);
        assertRejected(cart("M1", 2), "S", -1);
    }

    @Test
    void belowMinimumOrderRejected() {
        assertRejected(cart("M1", 1), "S", 1);
    }

    @Test
    void invalidAddressRejected() {
        assertRejected(cart("M1", 2), "  ", 1);
        assertRejected(cart("M1", 2), null, 1);
    }

    @Test
    void closedOrUnknownRestaurantRejected() {
        assertThrows(InvalidOrderException.class,
                () -> service.placeOrder("C1", "R2", cart("M9", 1), "S", 1));
        assertThrows(InvalidOrderException.class,
                () -> service.placeOrder("C1", "NOPE", cart("M1", 1), "S", 1));
        verify(orders, never()).save(any(Order.class));
    }

    @Test
    void pendingOrderCanBeCancelled() {
        Order o = new Order("O1", "C1", "R1");
        when(orders.findById("O1")).thenReturn(o);

        Order result = service.cancelOrder("O1");

        assertEquals("CANCELLED", result.getStatus());
        verify(orders).save(o);
    }

    @Test
    void orderInPreparationCannotBeCancelled() {
        Order o = new Order("O1", "C1", "R1");
        o.setStatus("PREPARING");
        when(orders.findById("O1")).thenReturn(o);

        assertThrows(InvalidOrderException.class, () -> service.cancelOrder("O1"));

        assertEquals("PREPARING", o.getStatus());
        verify(orders, never()).save(any(Order.class));
    }

    @Test
    void cancelUnknownOrderRejected() {
        when(orders.findById("none")).thenReturn(null);
        assertThrows(InvalidOrderException.class, () -> service.cancelOrder("none"));
    }
}
