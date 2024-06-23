package com.example.cafehub.dto.user;

import com.example.cafehub.dto.profilepicture.ProfilePictureResponseDto;
import java.util.List;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<ProfilePictureResponseDto> profilePictureUrls;
    private Long selectedProfilePictureId;
    private boolean isVerified;
}
