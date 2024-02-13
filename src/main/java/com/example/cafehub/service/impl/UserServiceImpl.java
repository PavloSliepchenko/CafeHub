package com.example.cafehub.service.impl;

import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.exception.RegistrationException;
import com.example.cafehub.mapper.UserMapper;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.UserRepository;
import com.example.cafehub.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final User.Role DEFAULT_ROLE = User.Role.USER;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format("The user with email %s already exists",
                    requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        user.setRole(DEFAULT_ROLE);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }
}
