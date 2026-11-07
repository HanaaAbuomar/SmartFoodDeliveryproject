package com.fooddelivery.service;

import com.fooddelivery.domain.Order;
import com.fooddelivery.domain.OrderStatus;
import com.fooddelivery.domain.order.TypedOrder;
import com.fooddelivery.exception.InvalidOrderException;
import com.fooddelivery.service.discount.DiscountStrategy;
import com.fooddelivery.service.payment.PaymentService;
import com.fooddelivery.service.payment.PaymentStatus;
import com.fooddelivery.service.strategy.PricingStrategy;
import com.fooddelivery.service.strategy.TimeProvider;

/**
 * Connects pricing, discounts, delivery fee, order type and payment for one order
 * (US4.2, US4.3, US4.4, US5.2).
 *
 * <p>Bill = discount(pricing(items total)) + delivery fee (by distance) + order-type fee.
 */
public class CheckoutService {
    private final DeliveryFeeCalculator feeCalculator;
    private final PaymentService paymentService;
    private final TimeProvider timeProvider;

    /**
     * @param feeCalculator  calculates the delivery fee from the distance
     * @param paymentService processes cash payments
     * @param timeProvider   supplies the current time (replaced by a mock in tests)
     */
    public CheckoutService(DeliveryFeeCalculator feeCalculator, PaymentService paymentService,
                           TimeProvider timeProvider) {
        this.feeCalculator = feeCalculator;
        this.paymentService = paymentService;
        this.timeProvider = timeProvider;
    }

    /**
     * Calculates the final bill of an order.
     *
     * @param order      the order
     * @param distanceKm the delivery distance in km
     * @param type       the order type, whose rules are applied
     * @param pricing    the pricing strategy
     * @param discount   the discount strategy
     * @return the bill
     * @throws IllegalArgumentException if an argument is null, the distance is negative or
     *                                  the order type rejects the order
     */
    public Bill calculateBill(Order order, double distanceKm, TypedOrder type,
                              PricingStrategy pricing, DiscountStrategy discount) {
        if (order == null || type == null || pricing == null || discount == null) {
            throw new IllegalArgumentException("Order, type, pricing and discount are required");
        }
        type.validate(timeProvider.getCurrentTime());
        double subtotal = order.getTotalPrice();
        double itemsTotal = discount.applyDiscount(pricing.calculatePrice(subtotal));
        double deliveryFee = feeCalculator.calculateFee(distanceKm) + type.getExtraDeliveryFee();
        return new Bill(subtotal, itemsTotal, deliveryFee);
    }

    /**
     * Pays a bill in cash and records the payment status on the order.
     *
     * @param order        the order being paid
     * @param bill         the bill to pay
     * @param cashProvided the cash given by the customer
     * @return SUCCESS if the cash covers the bill, otherwise FAILED
     * @throws InvalidOrderException if the order is cancelled or already paid
     */
    public PaymentStatus payCash(Order order, Bill bill, double cashProvided) {
        if (order == null || bill == null) {
            throw new IllegalArgumentException("Order and bill are required");
        }
        if (OrderStatus.CANCELLED.name().equals(order.getStatus())) {
            throw new InvalidOrderException("A cancelled order cannot be paid");
        }
        if (PaymentStatus.SUCCESS.name().equals(order.getPaymentStatus())) {
            throw new InvalidOrderException("Order is already paid");
        }
        PaymentStatus result = paymentService.processCashPayment(bill.getTotal(), cashProvided);
        order.setPaymentStatus(result.name());
        return result;
    }
}
