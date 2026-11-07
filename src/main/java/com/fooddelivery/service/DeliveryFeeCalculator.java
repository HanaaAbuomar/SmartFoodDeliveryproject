package com.fooddelivery.service;

/**
 * خدمة حساب رسوم التوصيل بناءً على المسافة المقطوعة بالكيلومترات.
 * <p>
 * تتضمن هذه الخدمة رسماً أساسياً ثابتاً بالإضافة إلى سعر متغيّر لكل كيلومتر.
 * </p>
 *
 * @author Hanaa
 * @version 1.0
 */
public class DeliveryFeeCalculator {

    private final double baseFee;
    private final double ratePerKm;

    /**
     * منشئ افتراضي يحدد الرسم الأساسي بـ 5.0 وسعر الكيلومتر بـ 1.5.
     */
    public DeliveryFeeCalculator() {
        this(5.0, 1.5);
    }

    /**
     * منشئ مخصص لتحديد قيم الرسم الأساسي وسعر الكيلومتر.
     *
     * @param baseFee   الرسم الأساسي الثابت للتوصيل
     * @param ratePerKm التكلفة المالية لكل كيلومتر
     */
    public DeliveryFeeCalculator(double baseFee, double ratePerKm) {
        this.baseFee = baseFee;
        this.ratePerKm = ratePerKm;
    }

    /**
     * يحسب إجمالي رسوم التوصيل المطلوبة بناءً على المسافة المحددة.
     *
     * @param distanceInKm المسافة بالكيلومتر (يجب ألا تكون سالبة)
     * @return إجمالي رسوم التوصيل المحسوبة
     * @throws IllegalArgumentException إذا كانت المسافة المدخلة قيمة سالبة
     */
    public double calculateFee(double distanceInKm) {
        if (distanceInKm < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        return baseFee + (distanceInKm * ratePerKm);
    }

    /**
     * يرجع قيمة الرسم الأساسي للتوصيل.
     *
     * @return قيمة baseFee
     */
    public double getBaseFee() {
        return baseFee;
    }

    /**
     * يرجع قيمة التكلفة لكل كيلومتر.
     *
     * @return قيمة ratePerKm
     */
    public double getRatePerKm() {
        return ratePerKm;
    }
}