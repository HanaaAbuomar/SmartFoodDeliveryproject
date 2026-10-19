package com.fooddelivery.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.fooddelivery.domain.MenuItem;
import com.fooddelivery.domain.Restaurant;
import com.fooddelivery.repository.RestaurantRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {
    @Mock
    RestaurantRepository repo;
    @InjectMocks
    RestaurantService service;

    @Test
    void onlyOpenRestaurantsShown() {
        Restaurant open = new Restaurant("R1", "Open Grill", "Amman", true);
        Restaurant closed = new Restaurant("R2", "Closed Cafe", "Amman", false);
        when(repo.findAll()).thenReturn(Arrays.asList(open, closed));

        List<Restaurant> result = service.getOpenRestaurants();

        assertEquals(1, result.size());
        assertEquals("R1", result.get(0).getId());
    }

    @Test
    void onlyAvailableItemsShownWithCorrectPrice() {
        Restaurant r = new Restaurant("R1", "Grill", "Amman", true);
        r.addMenuItem(new MenuItem("M1", "Burger", "Beef", 5.5, true));
        r.addMenuItem(new MenuItem("M2", "Fries", "Crispy", 2.0, false));
        when(repo.findById("R1")).thenReturn(r);

        List<MenuItem> menu = service.getAvailableMenu("R1");

        assertEquals(1, menu.size());
        assertEquals("Burger", menu.get(0).getName());
        assertEquals(5.5, menu.get(0).getPrice(), 0.001);
    }

    @Test
    void unknownRestaurantThrows() {
        when(repo.findById("nope")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> service.getAvailableMenu("nope"));
    }
}
