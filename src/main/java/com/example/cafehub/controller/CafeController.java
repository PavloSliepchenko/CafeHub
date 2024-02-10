package com.example.cafehub.controller;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.service.CafeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cafes")
public class CafeController {
    private final CafeService cafeService;

    @GetMapping
    public List<CafeResponseDto> getAllCafes(Pageable pageable) {
        return cafeService.getAllCafes(pageable);
    }

    @GetMapping(value = "/{cafeId}")
    public CafeResponseDto getCafeById(@PathVariable Long cafeId) {
        return cafeService.getCafeById(cafeId);
    }

    @GetMapping(value = "/search")
    public List<CafeResponseDto> searchCafesByParameters(CafeSearchParametersDto parametersDto) {
        return cafeService.cafeSearch(parametersDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CafeResponseDto addCafe(@Valid @RequestBody CreateCafeDto createCafeDto) {
        return cafeService.addCafe(createCafeDto);
    }

    @PutMapping(value = "/{cafeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CafeResponseDto updateCafeInfo(
            @PathVariable Long cafeId,
            @Valid @RequestBody CreateCafeDto createCafeDto
    ) {
        return cafeService.updateCafeInfo(cafeId, createCafeDto);
    }

    @DeleteMapping(value = "/{cafeId}")
    public void deleteCafe(@PathVariable Long cafeId) {
        cafeService.deleteCafe(cafeId);
    }
}
