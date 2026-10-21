package com.fooddelivery.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the MenuItem class.
 * Verifies item initialization, attributes, and getters/setters.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
 */
public class MenuItemTest {

    private MenuItem menuItem;

    @BeforeEach
    public void setUp() {
        // تهيئة طبق تجريبي قبل كل اختبار
        menuItem = new MenuItem("M-100", "Burger", "Juicy beef burger", 7.50, true);
    }

    @Test
    public void testMenuItemInitialization() {
        // التحقق من صحة القيم المعطاة للطبق عند الإنشاء
        assertEquals("M-100", menuItem.getId());
        assertEquals("Burger", menuItem.getName());
        assertEquals("Juicy beef burger", menuItem.getDescription());
        assertEquals(7.50, menuItem.getPrice(), 0.001);
        assertTrue(menuItem.isAvailable());
    }

    @Test
    public void testSettersAndGetters() {
        // التحقق من أن تعديل القيم عبر الـ Setters يعمل بشكل صحيح
        menuItem.setName("Chicken Burger");
        menuItem.setPrice(8.00);
        menuItem.setAvailable(false);

        assertEquals("Chicken Burger", menuItem.getName());
        assertEquals(8.00, menuItem.getPrice(), 0.001);
        assertFalse(menuItem.isAvailable());
    }
}