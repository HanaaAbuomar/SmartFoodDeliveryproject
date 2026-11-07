package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.order.ExpressOrder;
import com.fooddelivery.domain.order.ScheduledOrder;
import com.fooddelivery.domain.order.StandardOrder;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.service.discount.FixedAmountDiscountStrategy;
import com.fooddelivery.service.discount.NoDiscountStrategy;
import com.fooddelivery.service.discount.PercentageDiscountStrategy;
import com.fooddelivery.service.payment.PaymentService;
import com.fooddelivery.service.payment.PaymentStatus;
import com.fooddelivery.service.strategy.ExpressPricingStrategy;
import com.fooddelivery.service.strategy.NormalPricingStrategy;
import com.fooddelivery.service.strategy.PeakHourPricingStrategy;
import com.fooddelivery.service.strategy.TimeProvider;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CheckoutServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 11, 7, 17, 0);

    private TimeProvider time;
    private PaymentService payments;
    private CheckoutService checkout;
    private Order order;

    @BeforeEach
    void setUp() {
        time = mock(TimeProvider.class);
        when(time.getCurrentTime()).thenReturn(NOW);
        payments = mock(PaymentService.class);
        // delivery fee: 5 + 1.5 per km -> 10 km = 20
        checkout = new CheckoutService(new DeliveryFeeCalculator(), payments, time);
        order = new Order("O1", "C1", "R1");
        order.addItem(new MenuItem("M1", "Pizza", "x", 50.0, true));
        order.addItem(new MenuItem("M2", "Cola", "x", 50.0, true));
    }

    @Test
    void billIncludesDeliveryFeeByDistance() {
        Bill bill = checkout.calculateBill(order, 10.0, new StandardOrder("O1"),
                new NormalPricingStrategy(), new NoDiscountStrategy());
        assertEquals(100.0, bill.getSubtotal(), 0.001);
        assertEquals(100.0, bill.getItemsTotal(), 0.001);
        assertEquals(20.0, bill.getDeliveryFee(), 0.001);
        assertEquals(120.0, bill.getTotal(), 0.001);
    }

    @Test
    void expressOrderAddsExtraDeliveryFee() {
        Bill bill = checkout.calculateBill(order, 10.0, new ExpressOrder("O1"),
                new NormalPricingStrategy(), new NoDiscountStrategy());
        assertEquals(30.0, bill.getDeliveryFee(), 0.001);
        assertEquals(130.0, bill.getTotal(), 0.001);
    }

    @Test
    void pricingStrategyIsAppliedBeforeTheDiscount() {
        // express pricing: 100 + 10 = 110, then 10% off = 99, plus 20 delivery
        Bill bill = checkout.calculateBill(order, 10.0, new StandardOrder("O1"),
                new ExpressPricingStrategy(), new PercentageDiscountStrategy(0.10));
        assertEquals(99.0, bill.getItemsTotal(), 0.001);
        assertEquals(119.0, bill.getTotal(), 0.001);
    }

    @Test
    void fixedDiscountIsApplied() {
        Bill bill = checkout.calculateBill(order, 0.0, new StandardOrder("O1"),
                new NormalPricingStrategy(), new FixedAmountDiscountStrategy(15.0));
        assertEquals(85.0, bill.getItemsTotal(), 0.001);
        assertEquals(90.0, bill.getTotal(), 0.001); // 85 + base fee 5
    }

    @Test
    void peakHourPricingUsesTheMockedTime() {
        TimeProvider strategyTime = mock(TimeProvider.class);
        when(strategyTime.getCurrentTime()).thenReturn(LocalDateTime.of(2026, 11, 7, 13, 0));
        Bill bill = checkout.calculateBill(order, 0.0, new StandardOrder("O1"),
                new PeakHourPricingStrategy(strategyTime), new NoDiscountStrategy());
        assertEquals(130.0, bill.getItemsTotal(), 0.001);
    }

    @Test
    void scheduledOrderInThePastRelativeToTheClockIsRejected() {
        ScheduledOrder scheduled = new ScheduledOrder("O1", LocalDateTime.now().plusHours(1));
        // the mocked clock says it is already after the scheduled time
        when(time.getCurrentTime()).thenReturn(LocalDateTime.now().plusHours(2));
        assertThrows(IllegalArgumentException.class, () -> checkout.calculateBill(order, 5.0,
                scheduled, new NormalPricingStrategy(), new NoDiscountStrategy()));
    }

    @Test
    void negativeDistanceAndNullArgumentsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> checkout.calculateBill(order, -1.0,
                new StandardOrder("O1"), new NormalPricingStrategy(), new NoDiscountStrategy()));
        assertThrows(IllegalArgumentException.class, () -> checkout.calculateBill(null, 1.0,
                new StandardOrder("O1"), new NormalPricingStrategy(), new NoDiscountStrategy()));
    }

    @Test
    void successfulCashPaymentIsRecordedOnTheOrder() {
        Bill bill = new Bill(100, 100, 20);
        when(payments.processCashPayment(120.0, 150.0)).thenReturn(PaymentStatus.SUCCESS);
        assertEquals("UNPAID", order.getPaymentStatus());
        assertEquals(PaymentStatus.SUCCESS, checkout.payCash(order, bill, 150.0));
        assertEquals("SUCCESS", order.getPaymentStatus());
    }

    @Test
    void failedCashPaymentIsRecordedAndCanBeRetried() {
        Bill bill = new Bill(100, 100, 20);
        when(payments.processCashPayment(120.0, 50.0)).thenReturn(PaymentStatus.FAILED);
        when(payments.processCashPayment(120.0, 120.0)).thenReturn(PaymentStatus.SUCCESS);
        assertEquals(PaymentStatus.FAILED, checkout.payCash(order, bill, 50.0));
        assertEquals("FAILED", order.getPaymentStatus());
        assertEquals(PaymentStatus.SUCCESS, checkout.payCash(order, bill, 120.0));
        assertEquals("SUCCESS", order.getPaymentStatus());
    }

    @Test
    void anAlreadyPaidOrderCannotBePaidAgain() {
        order.setPaymentStatus("SUCCESS");
        assertThrows(InvalidOrderException.class,
                () -> checkout.payCash(order, new Bill(1, 1, 1), 10.0));
        verify(payments, never()).processCashPayment(anyDouble(), anyDouble());
    }

    @Test
    void aCancelledOrderCannotBePaid() {
        order.setStatus("CANCELLED");
        assertThrows(InvalidOrderException.class,
                () -> checkout.payCash(order, new Bill(1, 1, 1), 10.0));
    }

    @Test
    void payCashRejectsNullArguments() {
        assertThrows(IllegalArgumentException.class, () -> checkout.payCash(null, new Bill(1, 1, 1), 1));
        assertThrows(IllegalArgumentException.class, () -> checkout.payCash(order, null, 1));
    }
}
