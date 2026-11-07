package com.fooddelivery.service.strategy;

public class NormalPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount;
    }
}