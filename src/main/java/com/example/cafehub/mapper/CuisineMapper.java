package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.cuisine.CuisineResponseDto;
import com.example.cafehub.model.Cuisine;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CuisineMapper {
    CuisineResponseDto toDto(Cuisine cuisine);

    @Named("getByName")
    default List<Cuisine> getByName(List<String> cuisineNames) {
        if (cuisineNames == null) {
            return null;
        }

        List<Cuisine> cuisines = new ArrayList<>();
        for (String name : cuisineNames) {
            if (name.equalsIgnoreCase("Ukrainian")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(1L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("European")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(2L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Author")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(3L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Hosper")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(4L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Healthy")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(5L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Pastries and coffee")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(6L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Pizza")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(7L);
                cuisines.add(cuisine);
            } else if (name.equalsIgnoreCase("Fast food")) {
                Cuisine cuisine = new Cuisine();
                cuisine.setId(8L);
                cuisines.add(cuisine);
            }
        }
        return cuisines;
    }
}
