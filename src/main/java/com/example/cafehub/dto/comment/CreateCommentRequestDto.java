package com.example.cafehub.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateCommentRequestDto {
    @NotBlank
    private String comment;
    @NotNull
    @Min(1)
    @Max(5)
    private BigDecimal score;
}
