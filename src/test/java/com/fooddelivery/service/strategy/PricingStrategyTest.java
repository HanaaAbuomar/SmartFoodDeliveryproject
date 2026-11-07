package com.fooddelivery.service.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class PricingStrategyTest {

    @Test
    public void testNormalPricing() {
        PricingStrategy strategy = new NormalPricingStrategy();
        double price = strategy.calculatePrice(100.0);
        assertEquals(100.0, price, 0.001);
    }

    @Test
    public void testExpressPricing() {
        PricingStrategy strategy = new ExpressPricingStrategy();
        double price = strategy.calculatePrice(100.0);
        assertEquals(110.0, price, 0.001);
    }

    @Test
    public void testPromotionalPricing() {
        PricingStrategy strategy = new PromotionalPricingStrategy(0.20);
        double price = strategy.calculatePrice(100.0);
        assertEquals(80.0, price, 0.001);
    }

    @Test
    public void testPeakHourPricingDuringPeak() {
        TimeProvider timeProviderMock = Mockito.mock(TimeProvider.class);
        LocalDateTime peakTime = LocalDateTime.of(2026, 11, 7, 13, 0);
        when(timeProviderMock.getCurrentTime()).thenReturn(peakTime);

        PeakHourPricingStrategy strategy = new PeakHourPricingStrategy(timeProviderMock);
        double price = strategy.calculatePrice(100.0);
        
        assertEquals(130.0, price, 0.001);
    }

    @Test
    public void testPeakHourPricingOutsidePeak() {
        TimeProvider timeProviderMock = Mockito.mock(TimeProvider.class);
        LocalDateTime normalTime = LocalDateTime.of(2026, 11, 7, 17, 0);
        when(timeProviderMock.getCurrentTime()).thenReturn(normalTime);

        PeakHourPricingStrategy strategy = new PeakHourPricingStrategy(timeProviderMock);
        double price = strategy.calculatePrice(100.0);
        
        assertEquals(100.0, price, 0.001);
    }
}