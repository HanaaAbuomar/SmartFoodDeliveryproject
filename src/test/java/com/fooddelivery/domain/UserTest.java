package com.fooddelivery.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the User class.
 * Verifies user initialization, attributes, and role management.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
 */
public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        // تهيئة مستخدم تجريبي قبل كل اختبار
        user = new User("USER-01", "Hanaa Abuomar", "hanaa@stu.najah.edu", "secret123", "CUSTOMER");
    }

    @Test
    public void testUserInitialization() {
        // التحقق من صحة بيانات المستخدم عند إنشائه
        assertEquals("USER-01", user.getId());
        assertEquals("Hanaa Abuomar", user.getName());
        assertEquals("hanaa@stu.najah.edu", user.getEmail());
        assertEquals("secret123", user.getPassword());
        assertEquals("CUSTOMER", user.getRole());
    }

    @Test
    public void testUserSetters() {
        // التحقق من تعديل البيانات عبر الـ Setters
        user.setName("Heba Abuomar");
        user.setEmail("heba@najah.edu");
        user.setRole("MANAGER");

        assertEquals("Heba Abuomar", user.getName());
        assertEquals("heba@najah.edu", user.getEmail());
        assertEquals("MANAGER", user.getRole());
    }
}