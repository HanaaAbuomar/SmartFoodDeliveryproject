package com.fooddelivery.observer;

/**
 * Sends a message to a customer, restaurant or driver (US3.3).
 * Tests replace it with a Mockito mock; the real one can print, email, etc.
 */
public interface NotificationService {
    /**
     * @param recipientId id of the customer, restaurant or driver
     * @param message     the text to deliver
     */
    void send(String recipientId, String message);
}
