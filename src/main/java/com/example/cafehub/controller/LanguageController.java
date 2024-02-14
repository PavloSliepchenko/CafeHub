package com.example.cafehub.controller;

import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import com.example.cafehub.service.LanguageService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(value = "/languages")
public class LanguageController {
    private final LanguageService languageService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<LanguageResponseDto> getAllLanguages() {
        return languageService.getAllLanguages();
    }

    @GetMapping(value = "/{languageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public LanguageResponseDto getLanguageById(@PathVariable Long languageId) {
        return languageService.getLanguageById(languageId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public LanguageResponseDto addLanguage(
            @Valid @RequestBody LanguageCreateRequestDto createRequestDto) {
        return languageService.addLanguage(createRequestDto);
    }

    @PutMapping(value = "/{languageId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public LanguageResponseDto updateLanguage(
            @PathVariable Long languageId,
            @Valid @RequestBody LanguageCreateRequestDto createRequestDto
    ) {
        return languageService.updateLanguage(languageId, createRequestDto);
    }

    @DeleteMapping(value = "/{languageId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteLanguage(@PathVariable Long languageId) {
        languageService.deleteLanguageById(languageId);
    }
}
