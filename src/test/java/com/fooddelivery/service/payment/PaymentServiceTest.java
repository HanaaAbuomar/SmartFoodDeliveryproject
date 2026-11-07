package com.fooddelivery.service.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentServiceTest {

    @Test
    public void testCashPaymentSuccessExactAmount() {
        PaymentService paymentService = new PaymentService();
        PaymentStatus status = paymentService.processCashPayment(50.0, 50.0);
        assertEquals(PaymentStatus.SUCCESS, status);
    }

    @Test
    public void testCashPaymentSuccessMoreAmount() {
        PaymentService paymentService = new PaymentService();
        PaymentStatus status = paymentService.processCashPayment(50.0, 100.0);
        assertEquals(PaymentStatus.SUCCESS, status);
    }

    @Test
    public void testCashPaymentFailedInsufficientAmount() {
        PaymentService paymentService = new PaymentService();
        PaymentStatus status = paymentService.processCashPayment(50.0, 30.0);
        assertEquals(PaymentStatus.FAILED, status);
    }

    @Test
    public void testNegativeAmountsThrowException() {
        PaymentService paymentService = new PaymentService();
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processCashPayment(-10.0, 50.0);
        });
    }
}