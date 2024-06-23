package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.profilepicture.ProfilePictureResponseDto;
import com.example.cafehub.model.ProfilePicture;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ProfilePictureMapper {
    ProfilePictureResponseDto toDto(ProfilePicture profilePicture);
}
