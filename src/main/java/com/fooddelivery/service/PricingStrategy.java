package com.fooddelivery.service;

public interface PricingStrategy {
    double calculatePrice(double baseAmount);
}