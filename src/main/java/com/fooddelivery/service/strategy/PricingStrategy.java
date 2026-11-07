package com.fooddelivery.service.strategy;

public interface PricingStrategy {
    double calculatePrice(double baseAmount);
}