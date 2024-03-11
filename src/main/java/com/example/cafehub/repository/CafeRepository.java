package com.example.cafehub.repository;

import com.example.cafehub.model.Cafe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CafeRepository extends JpaRepository<Cafe, Long>, JpaSpecificationExecutor<Cafe> {
    List<Cafe> findByCity(String city, Pageable pageable);

    Optional<Cafe> findByNameAndAddressAndCity(String name, String address, String city);
}
