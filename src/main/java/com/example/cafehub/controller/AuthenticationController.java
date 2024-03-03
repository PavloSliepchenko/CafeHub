package com.example.cafehub.controller;

import com.example.cafehub.dto.user.UserLoginRequestDto;
import com.example.cafehub.dto.user.UserLoginResponseDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Tag(name = "Authentication", description = "End points for registration and login")
public class AuthenticationController {
    private final UserService userService;

    @PostMapping(value = "/register")
    @Operation(summary = "Register", description = "Saves a new user to DB")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto requestDto,
                                        HttpServletRequest httpRequest) {
        return userService.save(requestDto, httpRequest);
    }

    @PostMapping(value = "/login")
    @Operation(summary = "Login",
            description = "Checks if there is such user in DB and returns a token")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request,
                                      HttpServletRequest httpRequest) {
        return userService.login(request, httpRequest);
    }
}
