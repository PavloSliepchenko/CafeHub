package com.example.cafehub.repository;

import com.example.cafehub.model.Score;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    Optional<Score> findByUserIdAndCafeId(Long userId, Long cafeId);
}
