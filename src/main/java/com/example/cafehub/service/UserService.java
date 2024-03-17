package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserLoginRequestDto;
import com.example.cafehub.dto.user.UserLoginResponseDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {
    List<UserResponseDto> getAllUsers(Pageable pageable);

    UserResponseDto save(UserRegistrationRequestDto requestDto, HttpServletRequest httpRequest);

    UserLoginResponseDto login(UserLoginRequestDto request, HttpServletRequest httpRequest);

    UserWithRoleResponseDto updateRole(Long userId, String role);

    List<CafeResponseDto> getFavorites(Long userId);

    UserResponseDto updateAccountInfo(
            Long userId, UpdateAccountInfoDto requestDto, HttpServletRequest httpRequest);

    UserResponseDto updatePassword(Long userId, UpdatePasswordRequestDto requestDto);

    List<CafeResponseDto> addFavoriteCafe(Long userId, Long cafeId);

    List<CafeResponseDto> removeFavoriteCafe(Long userId, Long cafeId);

    String verifyEmail(String verificationCode);

    void resetPassword(PasswordResetDto resetDto);

    void deleteUserById(Long userId);
}
