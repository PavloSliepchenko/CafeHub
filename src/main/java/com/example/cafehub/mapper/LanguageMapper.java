package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import com.example.cafehub.model.Language;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface LanguageMapper {
    LanguageResponseDto toDto(Language language);

    Language toModel(LanguageCreateRequestDto createRequestDto);
}
