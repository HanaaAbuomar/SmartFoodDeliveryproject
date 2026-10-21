package com.fooddelivery.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Restaurant class.
 * Verifies restaurant initialization and menu management (adding/removing items).
 * 
 * @author Hanaa Abuomar
 * @version 1.0
 */
public class RestaurantTest {

    private Restaurant restaurant;
    private MenuItem item;

    @BeforeEach
    public void setUp() {
        // تهيئة مطعم تجريبي وطبق تجريبي قبل كل اختبار
        restaurant = new Restaurant("REST-01", "Al-Quds Restaurant", "Nablus Street", true);
        item = new MenuItem("M-1", "Pizza", "Cheese pizza", 12.00, true);
    }

    @Test
    public void testRestaurantInitialization() {
        // التأكد من صحة بيانات المطعم عند إنشائه
        assertEquals("REST-01", restaurant.getId());
        assertEquals("Al-Quds Restaurant", restaurant.getName());
        assertEquals("Nablus Street", restaurant.getAddress());
        assertTrue(restaurant.isOpen());
        assertNotNull(restaurant.getMenu());
        assertTrue(restaurant.getMenu().isEmpty());
    }

    @Test
    public void testAddMenuItem() {
        // اختبار إضافة طبق إلى قائمة المطعم
        restaurant.addMenuItem(item);

        assertEquals(1, restaurant.getMenu().size());
        assertEquals("Pizza", restaurant.getMenu().get(0).getName());
    }

    @Test
    public void testRemoveMenuItem() {
        // اختبار إضافة طبق ثم حذفه والتأكد أن القائمة أصبحت فارغة
        restaurant.addMenuItem(item);
        assertEquals(1, restaurant.getMenu().size());

        restaurant.removeMenuItem(item);
        assertTrue(restaurant.getMenu().isEmpty());
    }
}