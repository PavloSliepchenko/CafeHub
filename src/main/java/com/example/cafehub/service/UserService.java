package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import java.util.List;

public interface UserService {
    UserResponseDto save(UserRegistrationRequestDto requestDto);

    UserWithRoleResponseDto updateRole(Long userId, String role);

    List<CafeResponseDto> getFavorites(Long userId);

    UserResponseDto updateAccountInfo(Long userId, UpdateAccountInfoDto requestDto);

    UserResponseDto updatePassword(Long userId, UpdatePasswordRequestDto requestDto);

    List<CafeResponseDto> addFavoriteCafe(Long userId, Long cafeId);

    List<CafeResponseDto> removeFavoriteCafe(Long userId, Long cafeId);

    void resetPassword(PasswordResetDto resetDto);

    void deleteUserById(Long userId);
}
