package com.example.cafehub.dto.user;

import com.example.cafehub.validator.ValidFirstCapitalLetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateAccountInfoDto {
    @Email
    @NotNull
    @Length(max = 32)
    private String email;
    @NotNull
    @ValidFirstCapitalLetter
    @Length(min = 2, max = 40)
    private String firstName;
    @NotNull
    @ValidFirstCapitalLetter
    @Length(min = 2, max = 40)
    private String lastName;
}
