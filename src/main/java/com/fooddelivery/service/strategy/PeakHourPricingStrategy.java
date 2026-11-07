package com.fooddelivery.service.strategy;

import java.time.LocalTime;

public class PeakHourPricingStrategy implements PricingStrategy {
    private final TimeProvider timeProvider;

    public PeakHourPricingStrategy(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public double calculatePrice(double baseAmount) {
        LocalTime now = timeProvider.getCurrentTime().toLocalTime();
        // وقت الذروة بين 12:00 ظهراً و 2:00 عصراً كمثال
        boolean isPeak = !now.isBefore(LocalTime.of(12, 0)) && !now.isAfter(LocalTime.of(14, 0));
        
        if (isPeak) {
            return baseAmount * 1.30; // زيادة 30% وقت الذروة
        }
        return baseAmount;
    }
}