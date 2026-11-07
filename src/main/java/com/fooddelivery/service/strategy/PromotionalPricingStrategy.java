package com.fooddelivery.service.strategy;

public class PromotionalPricingStrategy implements PricingStrategy {
    private final double discountRate; // نسبة الخصم مثلاً 0.20 لخصم 20%

    public PromotionalPricingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount * (1.0 - discountRate);
    }
}