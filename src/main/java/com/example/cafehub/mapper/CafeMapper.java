package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.model.Cafe;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = {LanguageMapper.class, CommentMapper.class})
public interface CafeMapper {
    CafeResponseDto toDto(Cafe cafe);

    Cafe toModel(CreateCafeDto createCafeDto);
}
