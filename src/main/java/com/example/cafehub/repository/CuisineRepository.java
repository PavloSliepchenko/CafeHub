package com.example.cafehub.repository;

import com.example.cafehub.model.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuisineRepository extends JpaRepository<Cuisine, Long> {
}
