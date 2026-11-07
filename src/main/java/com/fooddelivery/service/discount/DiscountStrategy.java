package com.fooddelivery.service.discount;

public interface DiscountStrategy {
    double applyDiscount(double totalAmount);
}