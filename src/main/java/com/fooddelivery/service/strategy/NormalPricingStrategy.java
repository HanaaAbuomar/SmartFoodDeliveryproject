package com.fooddelivery.service.strategy;

/**
 * Normal pricing: the base amount is returned unchanged.
 */
public class NormalPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount;
    }
}
