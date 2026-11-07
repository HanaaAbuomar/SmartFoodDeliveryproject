package com.fooddelivery.service.strategy;

public class ExpressPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double baseAmount) {
        return baseAmount + 10.0; // إضافة رسوم ثابتة للتوصيل السريع
    }
}