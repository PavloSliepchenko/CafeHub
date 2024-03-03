package com.example.cafehub.controller;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.model.User;
import com.example.cafehub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
@Tag(name = "Users management",
        description = "Provides endpoints for CRUD operations with users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Get all users",
            description = "Returns all registered users. Available to admin users only")
    public List<UserResponseDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @PutMapping(value = "/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update user's role",
            description = "Updates user's role. Available to admin users only")
    public UserWithRoleResponseDto updateRole(@RequestParam Long userId,
                                              @RequestParam String role) {
        return userService.updateRole(userId, role);
    }

    @GetMapping(value = "/favorites")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Get favorite cafes",
            description = "Returns all user's favorite cafes. "
                    + "Available to all authenticated in users")
    public List<CafeResponseDto> getFavorites(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getFavorites(user.getId());
    }

    @PatchMapping(value = "/update")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update user's profile", description = "Updates user's profile info. "
            + "Available to all authenticated in users")
    public UserResponseDto updateProfile(Authentication authentication,
                                         @RequestBody UpdateAccountInfoDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateAccountInfo(user.getId(), requestDto);
    }

    @PutMapping(value = "/password")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Update password", description = "Updates user's password. "
            + "Available to all authenticated in users")
    public UserResponseDto updatePassword(Authentication authentication,
                                          @RequestBody UpdatePasswordRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updatePassword(user.getId(), requestDto);
    }

    @PatchMapping(value = "/reset")
    @Operation(summary = "Reset password",
            description = "Resets user's password and sends it to user's email")
    public void resetPassword(@RequestBody PasswordResetDto resetDto) {
        userService.resetPassword(resetDto);
    }

    @PostMapping(value = "/favorites/{cafeId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Add cafe to favorites", description = "Adds cafe to user's favorites. "
            + "Available to all authenticated in users")
    public List<CafeResponseDto> addCafeToFavorites(Authentication authentication,
                                                    @PathVariable Long cafeId) {
        User user = (User) authentication.getPrincipal();
        return userService.addFavoriteCafe(user.getId(), cafeId);
    }

    @DeleteMapping(value = "/favorites/{cafeId}")
    @PreAuthorize("hasAuthority('USER')")
    @Operation(summary = "Delete cafe from favorites",
            description = "Removes cafe from user's favorites. "
                    + "Available to all authenticated in users")
    public List<CafeResponseDto> deleteCafeFromFavorites(Authentication authentication,
                                                         @PathVariable Long cafeId) {
        User user = (User) authentication.getPrincipal();
        return userService.removeFavoriteCafe(user.getId(), cafeId);
    }

    @GetMapping(value = "/verify")
    @Operation(summary = "Verify email",
            description = "Verifies user's email with verification code")
    public String verify(@RequestParam String verificationCode) {
        return userService.verifyEmail(verificationCode);
    }

    @DeleteMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete user", description = "Deletes a user. Implements soft delete. "
            + "Available to admin users only")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
