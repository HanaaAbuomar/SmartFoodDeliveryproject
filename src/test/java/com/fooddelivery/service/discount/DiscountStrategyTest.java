package com.fooddelivery.service.discount;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountStrategyTest {

    @Test
    public void testPercentageDiscount() {
        DiscountStrategy discount = new PercentageDiscountStrategy(0.20); // 20% خصم
        double finalPrice = discount.applyDiscount(100.0);
        assertEquals(80.0, finalPrice, 0.001);
    }

    @Test
    public void testFixedAmountDiscount() {
        DiscountStrategy discount = new FixedAmountDiscountStrategy(15.0); // خصم 15
        double finalPrice = discount.applyDiscount(100.0);
        assertEquals(85.0, finalPrice, 0.001);
    }

    @Test
    public void testNoDiscount() {
        DiscountStrategy discount = new NoDiscountStrategy();
        double finalPrice = discount.applyDiscount(100.0);
        assertEquals(100.0, finalPrice, 0.001);
    }
    
    @Test
    public void testFixedAmountDiscountBelowZero() {
        DiscountStrategy discount = new FixedAmountDiscountStrategy(150.0); // خصم أكبر من المبلغ
        double finalPrice = discount.applyDiscount(100.0);
        assertEquals(0.0, finalPrice, 0.001); // يجب ألا يقل عن الصفر
    }
}