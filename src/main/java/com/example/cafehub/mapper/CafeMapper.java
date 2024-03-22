package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.model.Cafe;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class,
        uses = {LanguageMapper.class, CommentMapper.class, CuisineMapper.class})
public interface CafeMapper {
    CafeResponseDto toDto(Cafe cafe);

    @Mapping(target = "cuisines", source = "cuisineNames", qualifiedByName = "getByName")
    @Mapping(target = "languages", source = "languageNames", qualifiedByName = "getByLanguageName")
    Cafe toModel(CreateCafeDto createCafeDto);
}
