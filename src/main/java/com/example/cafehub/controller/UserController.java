package com.example.cafehub.controller;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.model.User;
import com.example.cafehub.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
public class UserController {
    private final UserService userService;

    @PutMapping(value = "/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserWithRoleResponseDto updateRole(@RequestParam Long userId,
                                              @RequestParam String role) {
        return userService.updateRole(userId, role);
    }

    @GetMapping(value = "/favorites")
    @PreAuthorize("hasAuthority('USER')")
    public List<CafeResponseDto> getFavorites(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getFavorites(user.getId());
    }

    @PatchMapping(value = "/update")
    @PreAuthorize("hasAuthority('USER')")
    public UserResponseDto updateProfile(Authentication authentication,
                                         @RequestBody UpdateAccountInfoDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updateAccountInfo(user.getId(), requestDto);
    }

    @PutMapping(value = "/password")
    @PreAuthorize("hasAuthority('USER')")
    public UserResponseDto updatePassword(Authentication authentication,
                                          @RequestBody UpdatePasswordRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return userService.updatePassword(user.getId(), requestDto);
    }

    @PatchMapping(value = "/reset")
    public void resetPassword(@RequestBody PasswordResetDto resetDto) {
        userService.resetPassword(resetDto);
    }

    @PostMapping(value = "/favorites/{cafeId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<CafeResponseDto> addCafeToFavorites(Authentication authentication,
                                                           @PathVariable Long cafeId) {
        User user = (User) authentication.getPrincipal();
        return userService.addFavoriteCafe(user.getId(), cafeId);
    }

    @DeleteMapping(value = "/favorites/{cafeId}")
    @PreAuthorize("hasAuthority('USER')")
    public List<CafeResponseDto> deleteCafeFromFavorites(Authentication authentication,
                                                         @PathVariable Long cafeId) {
        User user = (User) authentication.getPrincipal();
        return userService.removeFavoriteCafe(user.getId(), cafeId);
    }

    @DeleteMapping(value = "/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }
}
