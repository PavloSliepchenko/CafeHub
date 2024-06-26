package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CafeService {
    List<CafeResponseDto> getByName(String name, String city);

    List<CafeResponseDto> getAllCafes(Pageable pageable);

    List<CafeResponseDto> getAllCafesInCity(String cityName, Pageable pageable);

    CafeResponseDto getCafeById(Long cafeId);

    CafeResponseDto addCafe(CreateCafeDto createCafeDto);

    CafeResponseDto updateCafeInfo(Long cafeId, CreateCafeDto createCafeDto);

    List<CafeResponseDto> cafeSearch(CafeSearchParametersDto searchParametersDto);

    void deleteCafe(Long cafeId);
}
