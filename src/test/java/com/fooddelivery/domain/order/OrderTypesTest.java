package com.fooddelivery.domain.order;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class OrderTypesTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 7, 12, 0);

    @Test
    void standardOrderHasNoExtraFeeAndNoRules() {
        TypedOrder o = new StandardOrder("S1");
        assertEquals("STANDARD", o.getOrderType());
        assertEquals("S1", o.getOrderId());
        assertEquals(0.0, o.getExtraDeliveryFee(), 0.001);
        assertDoesNotThrow(() -> o.validate(NOW));
    }

    @Test
    void expressOrderAddsDefaultExtraFee() {
        TypedOrder o = new ExpressOrder("E1");
        assertEquals("EXPRESS", o.getOrderType());
        assertEquals(ExpressOrder.DEFAULT_EXTRA_FEE, o.getExtraDeliveryFee(), 0.001);
        assertDoesNotThrow(() -> o.validate(NOW));
    }

    @Test
    void expressOrderAcceptsCustomFee() {
        assertEquals(4.5, new ExpressOrder("E2", 4.5).getExtraDeliveryFee(), 0.001);
    }

    @Test
    void expressOrderRejectsNegativeFee() {
        assertThrows(IllegalArgumentException.class, () -> new ExpressOrder("E3", -1));
    }

    @Test
    void scheduledOrderDefaultsToScheduledType() {
        TypedOrder o = new ScheduledOrder("C1", LocalDateTime.now().plusHours(3));
        assertEquals("SCHEDULED", o.getOrderType());
        assertEquals(0.0, o.getExtraDeliveryFee(), 0.001);
    }

    @Test
    void scheduledOrderIsValidOnlyForAFutureTime() {
        LocalDateTime future = LocalDateTime.now().plusHours(3);
        ScheduledOrder o = new ScheduledOrder("C2", future);
        assertDoesNotThrow(() -> o.validate(LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class, () -> o.validate(future.plusMinutes(1)));
        assertThrows(IllegalArgumentException.class, () -> o.validate(future));
    }

    @Test
    void scheduledOrderWithoutTimeIsInvalid() {
        ScheduledOrder o = new ScheduledOrder("C3", null);
        assertThrows(IllegalArgumentException.class, () -> o.validate(NOW));
    }
}
