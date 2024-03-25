package com.example.cafehub.repository;

import com.example.cafehub.model.Cafe;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CafeRepositoryTest {
    @Autowired
    private CafeRepository cafeRepository;

    @Test
    @DisplayName("Find cafe by name in the city")
    void findByName_ValidRequest_ShouldReturnCafes() {
        String city = "Kyiv";
        List<Cafe> cafes = cafeRepository.findByName("love", city);
        Assertions.assertEquals(3, cafes.size());
        Assertions.assertEquals("One Love", cafes.get(0).getName());
        Assertions.assertEquals(city, cafes.get(0).getCity());

        cafes = cafeRepository.findByName("o", city);
        Assertions.assertEquals(16, cafes.size());
        Assertions.assertEquals(city, cafes.get(0).getCity());

        cafes = cafeRepository.findByName("boutique", city);
        Assertions.assertEquals(3, cafes.size());
        Assertions.assertEquals(city, cafes.get(0).getCity());

        city = "Lviv";
        cafes = cafeRepository.findByName("a", city);
        Assertions.assertTrue(cafes.isEmpty());
    }
}
