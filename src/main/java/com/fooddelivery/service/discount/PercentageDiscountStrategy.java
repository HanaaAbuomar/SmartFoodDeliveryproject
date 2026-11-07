package com.fooddelivery.service.discount;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private double percentage; // مثال: 0.15 تعني 15%

    public PercentageDiscountStrategy(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount - (totalAmount * percentage);
    }
}