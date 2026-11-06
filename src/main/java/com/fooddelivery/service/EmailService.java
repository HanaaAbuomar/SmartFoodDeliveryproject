package com.fooddelivery.service; 
 
public interface EmailService { 
    void sendOrderConfirmation(String orderId, String customerEmail); 
} 
