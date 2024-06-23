package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = ProfilePictureMapper.class)
public interface UserMapper {
    @Mapping(target = "profilePictureUrls", source = "profilePictureUrls")
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);

    UserWithRoleResponseDto toDtoWithRole(User user);
}
