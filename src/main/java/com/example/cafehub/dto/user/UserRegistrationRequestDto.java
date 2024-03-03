package com.example.cafehub.dto.user;

import com.example.cafehub.validator.FieldMatch;
import com.example.cafehub.validator.ValidFirstCapitalLetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Passwords do not match!"
        )
public class UserRegistrationRequestDto {
    @Email
    @NotNull
    @Length(max = 32)
    private String email;
    @NotNull
    @Length(min = 5, max = 15)
    private String password;
    @NotNull
    @Length(min = 5, max = 15)
    private String repeatPassword;
    @NotNull
    @ValidFirstCapitalLetter
    @Length(min = 2, max = 40)
    private String firstName;
    @NotNull
    @ValidFirstCapitalLetter
    @Length(min = 2, max = 40)
    private String lastName;
}
