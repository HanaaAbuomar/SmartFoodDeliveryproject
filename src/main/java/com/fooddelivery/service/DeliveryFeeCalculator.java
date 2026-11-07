package com.fooddelivery.service;

public class DeliveryFeeCalculator {

    private final double baseFee;
    private final double ratePerKm;

    // افتراضياً: رسم أساسي 5.0 وسعر الكيلومتر 1.5
    public DeliveryFeeCalculator() {
        this(5.0, 1.5);
    }

    public DeliveryFeeCalculator(double baseFee, double ratePerKm) {
        this.baseFee = baseFee;
        this.ratePerKm = ratePerKm;
    }

    /**
     * حساب رسوم التوصيل بناءً على المسافة بالكيلومتر
     */
    public double calculateFee(double distanceInKm) {
        if (distanceInKm < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        return baseFee + (distanceInKm * ratePerKm);
    }

    public double getBaseFee() {
        return baseFee;
    }

    public double getRatePerKm() {
        return ratePerKm;
    }
}