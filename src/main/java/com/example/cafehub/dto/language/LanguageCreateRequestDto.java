package com.example.cafehub.dto.language;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LanguageCreateRequestDto {
    @NotEmpty
    @NotNull
    private String name;
}
