package com.fooddelivery.domain.order;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class ScheduledOrderTest {

    @Test
    public void testValidScheduledOrderInFuture() {
        LocalDateTime futureTime = LocalDateTime.now().plusHours(2);
        ScheduledOrder order = new ScheduledOrder("ORD-101", futureTime, "SCHEDULED");
        
        assertEquals("ORD-101", order.getOrderId());
        assertEquals("SCHEDULED", order.getOrderType());
        assertEquals(futureTime, order.getScheduledTime());
    }

    @Test
    public void testScheduledOrderInPastThrowsException() {
        LocalDateTime pastTime = LocalDateTime.now().minusHours(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            new ScheduledOrder("ORD-102", pastTime, "SCHEDULED");
        });
    }
}