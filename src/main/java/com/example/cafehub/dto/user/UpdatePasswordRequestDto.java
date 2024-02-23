package com.example.cafehub.dto.user;

import com.example.cafehub.validator.FieldMatch;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(
        field = "newPassword",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
)
public class UpdatePasswordRequestDto {
    @NotNull
    @Length(min = 5, max = 15)
    private String newPassword;
    @NotNull
    @Length(min = 5, max = 15)
    private String repeatPassword;
}
