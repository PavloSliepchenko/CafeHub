package com.example.cafehub.dto.user;

import jakarta.validation.constraints.NotNull;

public record PasswordResetDto(@NotNull String email) {

}
