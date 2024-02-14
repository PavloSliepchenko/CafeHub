package com.example.cafehub.service;

import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import java.util.List;

public interface LanguageService {
    LanguageResponseDto getLanguageById(Long languageId);

    List<LanguageResponseDto> getAllLanguages();

    LanguageResponseDto addLanguage(LanguageCreateRequestDto createRequestDto);

    LanguageResponseDto updateLanguage(Long languageId, LanguageCreateRequestDto createRequestDto);

    void deleteLanguageById(Long languageId);
}
