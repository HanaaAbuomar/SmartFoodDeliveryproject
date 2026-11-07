package com.fooddelivery.observer;

/** Simple {@link NotificationService} that prints notifications to the console. */
public class ConsoleNotificationService implements NotificationService {

    @Override
    public void send(String recipientId, String message) {
        System.out.println("[NOTIFY -> " + recipientId + "] " + message);
    }
}
