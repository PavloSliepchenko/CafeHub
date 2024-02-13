package com.example.cafehub.controller;

import com.example.cafehub.dto.user.UserLoginRequestDto;
import com.example.cafehub.dto.user.UserLoginResponseDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.security.AuthenticationService;
import com.example.cafehub.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping(value = "/register")
    public UserResponseDto registerUser(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.save(request);
    }

    @PostMapping(value = "/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto request) {
        return authenticationService.authenticate(request);
    }
}
