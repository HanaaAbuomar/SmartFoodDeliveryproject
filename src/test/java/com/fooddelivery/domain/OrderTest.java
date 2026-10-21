package com.fooddelivery.domain;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Order class.
 * Verifies order initialization, item management, and price calculations.
 * 
 * @author Hanaa Abuomar
 * @version 1.0
 */
public class OrderTest {

    private Order order;
    private MenuItem item1;
    private MenuItem item2;

    @BeforeEach
    public void setUp() {
        // تهيئة بيانات تجريبية قبل كل اختبار
        order = new Order("ORD-101", "CUST-01", "REST-01");
        item1 = new MenuItem("M-1", "Burger", "Delicious cheese burger", 8.50, true);
        item2 = new MenuItem("M-2", "Pepsi", "Cold soft drink", 2.00, true);
    }

    @Test
    public void testOrderInitialization() {
        // التأكد من أن الطلب يبدأ بقائمة فارغة وبحالة PENDING والسعر صفر
        assertNotNull(order.getItems());
        assertTrue(order.getItems().isEmpty());
        assertEquals(0.0, order.getTotalPrice());
        assertEquals("PENDING", order.getStatus());
        assertEquals("ORD-101", order.getOrderId());
    }

    @Test
    public void testAddItemAndUpdatePrice() {
        // إضافة عنصر والتحقق من تحديث السعر وحجم القائمة
        order.addItem(item1);
        order.addItem(item2);

        assertEquals(2, order.getItems().size());
        assertEquals(10.50, order.getTotalPrice(), 0.001); // 8.50 + 2.00
    }

    @Test
    public void testRemoveItemAndUpdatePrice() {
        // إضافة عناصر ثم حذف عنصر والتحقق من تعديل السعر
        order.addItem(item1);
        order.addItem(item2);
        
        order.removeItem(item1);

        assertEquals(1, order.getItems().size());
        assertEquals(2.00, order.getTotalPrice(), 0.001);
    }
}