package com.fooddelivery.domain;

import static com.fooddelivery.domain.OrderStatus.CANCELLED;
import static com.fooddelivery.domain.OrderStatus.CONFIRMED;
import static com.fooddelivery.domain.OrderStatus.DELIVERED;
import static com.fooddelivery.domain.OrderStatus.OUT_FOR_DELIVERY;
import static com.fooddelivery.domain.OrderStatus.PENDING;
import static com.fooddelivery.domain.OrderStatus.PREPARING;
import static com.fooddelivery.domain.OrderStatus.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fooddelivery.exception.InvalidOrderException;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void fullLifecycleIsAccepted() {
        assertTrue(PENDING.canTransitionTo(CONFIRMED));
        assertTrue(CONFIRMED.canTransitionTo(PREPARING));
        assertTrue(PREPARING.canTransitionTo(READY));
        assertTrue(READY.canTransitionTo(OUT_FOR_DELIVERY));
        assertTrue(OUT_FOR_DELIVERY.canTransitionTo(DELIVERED));
    }

    @Test
    void skippingStatesIsRejected() {
        assertFalse(PENDING.canTransitionTo(PREPARING));
        assertFalse(PENDING.canTransitionTo(DELIVERED));
        assertFalse(CONFIRMED.canTransitionTo(READY));
        assertFalse(READY.canTransitionTo(DELIVERED));
    }

    @Test
    void goingBackwardsIsRejected() {
        assertFalse(CONFIRMED.canTransitionTo(PENDING));
        assertFalse(READY.canTransitionTo(PREPARING));
        assertFalse(DELIVERED.canTransitionTo(OUT_FOR_DELIVERY));
    }

    @Test
    void onlyPendingOrdersCanBeCancelled() {
        for (OrderStatus s : OrderStatus.values()) {
            assertEquals(s == PENDING, s.canTransitionTo(CANCELLED), "from " + s);
        }
    }

    @Test
    void deliveredAndCancelledAreFinal() {
        for (OrderStatus finalState : new OrderStatus[] {DELIVERED, CANCELLED}) {
            assertTrue(finalState.isTerminal());
            assertTrue(finalState.allowedNext().isEmpty());
            for (OrderStatus target : OrderStatus.values()) {
                assertFalse(finalState.canTransitionTo(target));
            }
        }
    }

    @Test
    void otherStatesAreNotFinal() {
        assertFalse(PENDING.isTerminal());
        assertFalse(CONFIRMED.isTerminal());
        assertFalse(PREPARING.isTerminal());
        assertFalse(READY.isTerminal());
        assertFalse(OUT_FOR_DELIVERY.isTerminal());
    }

    @Test
    void nullTargetIsRejected() {
        assertFalse(PENDING.canTransitionTo(null));
    }

    @Test
    void fromStringIgnoresCaseSpacesAndHyphens() {
        assertEquals(PENDING, OrderStatus.fromString("pending"));
        assertEquals(OUT_FOR_DELIVERY, OrderStatus.fromString("Out for delivery"));
        assertEquals(OUT_FOR_DELIVERY, OrderStatus.fromString(" out-for-delivery "));
        assertEquals(DELIVERED, OrderStatus.fromString("DELIVERED"));
    }

    @Test
    void fromStringRejectsUnknownOrMissingText() {
        assertThrows(InvalidOrderException.class, () -> OrderStatus.fromString("FLYING"));
        assertThrows(InvalidOrderException.class, () -> OrderStatus.fromString(null));
        assertThrows(InvalidOrderException.class, () -> OrderStatus.fromString("   "));
    }

    @Test
    void everyStatusHasADescription() {
        for (OrderStatus s : OrderStatus.values()) {
            assertFalse(s.getDescription().isEmpty());
        }
        assertEquals("is out for delivery", OUT_FOR_DELIVERY.getDescription());
    }
}
