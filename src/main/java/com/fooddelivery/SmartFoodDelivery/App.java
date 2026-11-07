package com.fooddelivery.SmartFoodDelivery;

import com.fooddelivery.service.RealEmailSender;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting real email sending process...");

        // ضعي هنا إيميلك الحقيقي الذي تريدين استقبال الرسالة عليه
        String recipient = "hanaaabuomar6@gmail.com";
        String subject = "Smart Food Delivery - Test Notification";
        String body = "Hello! This is a real email sent successfully from our Java project code using .env configuration.";

        // استدعاء دالة الإرسال الحقيقي
        RealEmailSender.sendRealEmail(recipient, subject, body);
    }
}