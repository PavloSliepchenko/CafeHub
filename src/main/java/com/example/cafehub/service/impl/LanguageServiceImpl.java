package com.example.cafehub.service.impl;

import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import com.example.cafehub.exception.EntityAlreadyExistsException;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.LanguageMapper;
import com.example.cafehub.model.Language;
import com.example.cafehub.repository.LanguageRepository;
import com.example.cafehub.service.LanguageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public LanguageResponseDto getLanguageById(Long languageId) {
        return languageMapper.toDto(getLanguageWithId(languageId));
    }

    @Override
    public List<LanguageResponseDto> getAllLanguages() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .toList();
    }

    @Override
    public LanguageResponseDto addLanguage(LanguageCreateRequestDto createRequestDto) {
        if (languageRepository.findByName(createRequestDto.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("This language was added before. "
                    + createRequestDto.getName());
        }
        return languageMapper.toDto(
                languageRepository.save(
                        languageMapper.toModel(createRequestDto)));
    }

    @Override
    public LanguageResponseDto updateLanguage(Long languageId,
                                              LanguageCreateRequestDto createRequestDto) {
        if (languageRepository.findByName(createRequestDto.getName()).isPresent()) {
            throw new EntityAlreadyExistsException("This language was added before. "
                    + createRequestDto.getName());
        }
        Language language = getLanguageWithId(languageId);
        language.setName(createRequestDto.getName());
        return languageMapper.toDto(languageRepository.save(language));
    }

    @Override
    public void deleteLanguageById(Long languageId) {
        languageRepository.delete(getLanguageWithId(languageId));
    }

    private Language getLanguageWithId(Long languageId) {
        return languageRepository.findById(languageId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no language with id " + languageId));
    }
}
