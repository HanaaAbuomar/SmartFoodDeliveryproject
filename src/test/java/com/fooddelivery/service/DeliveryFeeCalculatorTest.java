package com.fooddelivery.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeliveryFeeCalculatorTest {

    @Test
    public void testCalculateFeeDefaultRates() {
        DeliveryFeeCalculator calculator = new DeliveryFeeCalculator();
        // Base: 5.0 + (10 km * 1.5) = 20.0
        double fee = calculator.calculateFee(10.0);
        assertEquals(20.0, fee, 0.001);
    }

    @Test
    public void testCalculateFeeCustomRates() {
        DeliveryFeeCalculator calculator = new DeliveryFeeCalculator(3.0, 2.0);
        // Base: 3.0 + (5 km * 2.0) = 13.0
        double fee = calculator.calculateFee(5.0);
        assertEquals(13.0, fee, 0.001);
    }

    @Test
    public void testCalculateFeeZeroDistance() {
        DeliveryFeeCalculator calculator = new DeliveryFeeCalculator();
        // Distance 0 km -> fee is base fee only (5.0)
        double fee = calculator.calculateFee(0.0);
        assertEquals(5.0, fee, 0.001);
    }

    @Test
    public void testCalculateFeeNegativeDistanceThrowsException() {
        DeliveryFeeCalculator calculator = new DeliveryFeeCalculator();
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateFee(-5.0);
        });
    }
}