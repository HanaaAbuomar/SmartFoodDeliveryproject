package com.fooddelivery.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class RealEmailSender {

    public static void sendRealEmail(String recipientEmail, String subject, String messageBody) {
        // تحميل متغيرات البيئة من ملف .env
        Dotenv dotenv = Dotenv.load();
        String username = dotenv.get("MAIL_USERNAME");
        String password = dotenv.get("MAIL_PASSWORD");

        // إعدادات اتصال سيرفر SMTP الخاص بـ Gmail
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(recipientEmail)
            );
            message.setSubject(subject);
            message.setText(messageBody);

            // إرسال الإيميل الفعلي
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}