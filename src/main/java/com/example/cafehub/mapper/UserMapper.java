package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    UserWithRoleResponseDto toDtoWithRole(User user);
}
