package com.example.cafehub.controller;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.model.User;
import com.example.cafehub.service.CafeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cafes")
@Tag(name = "Cafes management", description = "Provides endpoints for CRUD operations with cafes")
public class CafeController {
    private final CafeService cafeService;

    @GetMapping(value = "/name")
    @Operation(summary = "Get cafes by name", description = "Returns cafes with the similar name")
    public List<CafeResponseDto> getCafeByName(@RequestParam String name,
                                               @RequestParam String city) {
        return cafeService.getByName(name, city);
    }

    @GetMapping
    @Operation(summary = "Get all cafes", description = "Returns all cafes from DB")
    public List<CafeResponseDto> getAllCafes(Pageable pageable) {
        return cafeService.getAllCafes(pageable);
    }

    @GetMapping(value = "/city")
    @Operation(summary = "Get all cafes in the city",
            description = "Returns all cafes in a chosen city")
    public List<CafeResponseDto> getAllCafesInCity(@RequestParam String city, Pageable pageable) {
        return cafeService.getAllCafesInCity(city, pageable);
    }

    @GetMapping(value = "/{cafeId}")
    @Operation(summary = "Get cafe by id", description = "Returns a cafe by its id")
    public CafeResponseDto getCafeById(@PathVariable Long cafeId) {
        return cafeService.getCafeById(cafeId);
    }

    @GetMapping(value = "/search")
    @Operation(summary = "Search for cafes by parameters",
            description = "Returns all cafes that satisfy selected parameters")
    public List<CafeResponseDto> searchCafesByParameters(
            @Valid CafeSearchParametersDto parametersDto) {
        return cafeService.cafeSearch(parametersDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Add cafe",
            description = "Adds a new cafe to DB. Available to admin users only")
    public CafeResponseDto addCafe(@Valid @RequestBody CreateCafeDto createCafeDto) {
        return cafeService.addCafe(createCafeDto);
    }

    @PutMapping(value = "/{cafeId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update cafe's info",
            description = "Updates cafes info. Available to admin users only")
    public CafeResponseDto updateCafeInfo(
            @PathVariable Long cafeId,
            @Valid @RequestBody CreateCafeDto createCafeDto
    ) {
        return cafeService.updateCafeInfo(cafeId, createCafeDto);
    }

    @PostMapping(value = "/scores")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Set a score to cafe",
            description = "Sets a score to cafe. Available to all authenticated in users")
    public CafeResponseDto setScore(Authentication authentication,
                                    @RequestParam Long cafeId,
                                    @Min(1)
                                    @Max(5)
                                    @RequestParam BigDecimal score) {
        User user = (User) authentication.getPrincipal();
        return cafeService.setScore(user.getId(), cafeId, score);
    }

    @DeleteMapping(value = "/{cafeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cafe",
            description = "Deletes a chosen cafe. Available to admin users only. "
                    + "Implements soft delete")
    public void deleteCafe(@PathVariable Long cafeId) {
        cafeService.deleteCafe(cafeId);
    }
}
