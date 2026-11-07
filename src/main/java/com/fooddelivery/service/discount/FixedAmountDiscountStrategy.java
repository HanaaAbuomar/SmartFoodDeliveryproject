package com.fooddelivery.service.discount;

public class FixedAmountDiscountStrategy implements DiscountStrategy {
    private double discountAmount;

    public FixedAmountDiscountStrategy(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double totalAmount) {
        double result = totalAmount - discountAmount;
        return result < 0 ? 0.0 : result; // نتأكد أن السعر لا يصبح بالسالب
    }
}