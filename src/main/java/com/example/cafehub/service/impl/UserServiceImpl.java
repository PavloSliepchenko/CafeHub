package com.example.cafehub.service.impl;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.exception.RegistrationException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.mapper.UserMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.UserRepository;
import com.example.cafehub.service.EmailService;
import com.example.cafehub.service.PasswordGeneratorService;
import com.example.cafehub.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final User.Role DEFAULT_ROLE = User.Role.USER;
    private static final Random random = new Random();
    private final PasswordGeneratorService passwordGenerator;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final EmailService emailService;
    private final CafeMapper cafeMapper;
    private final UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto) {
        if (findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format("The user with email %s already exists",
                    requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        user.setRole(DEFAULT_ROLE);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserWithRoleResponseDto updateRole(Long userId, String role) {
        User user = getUserById(userId);
        user.setRole(User.Role.valueOf(role));
        return userMapper.toDtoWithRole(userRepository.save(user));
    }

    @Override
    public List<CafeResponseDto> getFavorites(Long userId) {
        return getUserById(userId).getFavorite().stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto updateAccountInfo(Long userId, UpdateAccountInfoDto requestDto) {
        User user = getUserById(userId);
        user.setEmail(requestDto.getEmail());
        user.setLastName(requestDto.getLastName());
        user.setFirstName(requestDto.getFirstName());
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserResponseDto updatePassword(Long userId, UpdatePasswordRequestDto requestDto) {
        User user = getUserById(userId);
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public List<CafeResponseDto> addFavoriteCafe(Long userId, Long cafeId) {
        User user = getUserById(userId);
        List<Cafe> favorite = user.getFavorite();
        Optional<Cafe> favoriteCafe = favorite.stream()
                .filter(e -> e.getId().equals(cafeId))
                .findFirst();
        if (favoriteCafe.isPresent()) {
            throw new RuntimeException(String.format(
                    "Cafe with id %s was added to favorites before", cafeId));
        }
        Cafe cafe = cafeRepository.findById(cafeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no cafe with id " + cafeId));
        favorite.add(cafe);
        return userRepository.save(user).getFavorite().stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public List<CafeResponseDto> removeFavoriteCafe(Long userId, Long cafeId) {
        User user = getUserById(userId);
        List<Cafe> favorite = user.getFavorite();
        Optional<Cafe> favoriteCafe = favorite.stream()
                .filter(e -> e.getId().equals(cafeId))
                .findFirst();
        if (favoriteCafe.isEmpty()) {
            throw new RuntimeException(String.format(
                    "Cafe with id %s was removed from favorites before", cafeId));
        }
        favorite.remove(favoriteCafe.get());
        return userRepository.save(user).getFavorite().stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public void resetPassword(PasswordResetDto resetDto) {
        Optional<User> userOptional = findUserByEmail(resetDto.email());
        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("There is no user with email " + resetDto.email());
        }
        String newPassword = passwordGenerator.generateRandomPassword(random.nextInt(5, 10));
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendEmail(resetDto.email(), newPassword);
    }

    @Override
    public void deleteUserById(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no user with id " + userId));
    }

    private Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
