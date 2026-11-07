package com.fooddelivery.service.strategy;

import java.time.LocalDateTime;

public class TimeProvider {
    // ترجع الوقت الحالي الحقيقي، ويمكن عمل Mock لها في الاختبارات
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}