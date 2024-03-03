package com.example.cafehub.dto.user;

public record UserLoginResponseDto(String firstName,
                                   String lastName,
                                   String email,
                                   String token) {
}
