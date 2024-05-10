package com.example.cafehub.dto.user;

import com.example.cafehub.validator.ValidFirstCapitalLetter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateAccountInfoDto {
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
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
