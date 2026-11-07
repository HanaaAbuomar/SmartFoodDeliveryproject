package com.fooddelivery.service.discount;

public class NoDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double totalAmount) {
        return totalAmount; // يبقى السعر كما هو بدون أي تعديل
    }
}