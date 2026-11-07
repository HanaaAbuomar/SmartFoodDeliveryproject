package com.fooddelivery.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fooddelivery.driver.CarDriver;
import com.fooddelivery.driver.Driver;
import org.junit.jupiter.api.Test;

class InMemoryDriverRepositoryTest {
    private final InMemoryDriverRepository repo = new InMemoryDriverRepository();

    @Test
    void savedDriverCanBeFoundById() {
        Driver d = new CarDriver("D1", "Sam", 0, 0);
        repo.save(d);

        assertSame(d, repo.findById("D1"));
    }

    @Test
    void savingAgainReplacesTheDriver() {
        repo.save(new CarDriver("D1", "Old", 0, 0));
        Driver updated = new CarDriver("D1", "New", 0, 0);
        repo.save(updated);

        assertSame(updated, repo.findById("D1"));
        assertEquals(1, repo.findAll().size());
    }

    @Test
    void unknownOrNullIdReturnsNull() {
        assertNull(repo.findById("nope"));
        assertNull(repo.findById(null));
    }

    @Test
    void findAllReturnsACopy() {
        repo.save(new CarDriver("D1", "Sam", 0, 0));
        repo.save(new CarDriver("D2", "Lee", 0, 0));

        assertEquals(2, repo.findAll().size());
        repo.findAll().clear();
        assertEquals(2, repo.findAll().size());
    }

    @Test
    void emptyRepositoryHasNoDrivers() {
        assertTrue(repo.findAll().isEmpty());
    }

    @Test
    void nullDriverOrNullIdIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> repo.save(null));
        assertThrows(IllegalArgumentException.class,
                () -> repo.save(new CarDriver(null, "Sam", 0, 0)));
    }
}
