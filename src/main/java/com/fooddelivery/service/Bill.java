package com.fooddelivery.service;

/**
 * The final bill of an order (US4.2, US4.3): items after pricing and discount, plus delivery.
 */
public class Bill {
    private final double subtotal;
    private final double itemsTotal;
    private final double deliveryFee;

    /**
     * @param subtotal    the sum of the item prices
     * @param itemsTotal  the items amount after the pricing strategy and the discount
     * @param deliveryFee the delivery fee, including any order-type surcharge
     */
    public Bill(double subtotal, double itemsTotal, double deliveryFee) {
        this.subtotal = subtotal;
        this.itemsTotal = itemsTotal;
        this.deliveryFee = deliveryFee;
    }

    /** @return the sum of the item prices before pricing and discount */
    public double getSubtotal() {
        return subtotal;
    }

    /** @return the items amount after the pricing strategy and the discount */
    public double getItemsTotal() {
        return itemsTotal;
    }

    /** @return the delivery fee that is part of the final bill */
    public double getDeliveryFee() {
        return deliveryFee;
    }

    /** @return the final amount the customer pays: items plus delivery fee */
    public double getTotal() {
        return itemsTotal + deliveryFee;
    }
}
