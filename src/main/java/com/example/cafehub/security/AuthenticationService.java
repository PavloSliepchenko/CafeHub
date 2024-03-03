package com.example.cafehub.security;

import com.example.cafehub.dto.user.UserLoginRequestDto;
import com.example.cafehub.dto.user.UserLoginResponseDto;
import com.example.cafehub.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserLoginResponseDto authenticate(UserLoginRequestDto requestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.email(),
                        requestDto.password())
        );
        String token = jwtUtil.generateToken(authentication.getName());
        User user = (User) authentication.getPrincipal();
        return new UserLoginResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                token
        );
    }
}
