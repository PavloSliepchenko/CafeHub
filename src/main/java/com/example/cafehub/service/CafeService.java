package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CafeService {
    List<CafeResponseDto> getAllCafes(Pageable pageable);

    List<CafeResponseDto> getAllCafesInCity(String cityName, Pageable pageable);

    CafeResponseDto getCafeById(Long cafeId);

    CafeResponseDto addCafe(CreateCafeDto createCafeDto);

    CafeResponseDto updateCafeInfo(Long cafeId, CreateCafeDto createCafeDto);

    List<CafeResponseDto> cafeSearch(CafeSearchParametersDto searchParametersDto);

    CafeResponseDto setScore(Long userId, Long cafeId, BigDecimal score);

    void deleteCafe(Long cafeId);
}
