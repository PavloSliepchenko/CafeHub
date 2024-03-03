package com.example.cafehub.service.impl;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.PasswordResetDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
import com.example.cafehub.dto.user.UpdatePasswordRequestDto;
import com.example.cafehub.dto.user.UserLoginRequestDto;
import com.example.cafehub.dto.user.UserLoginResponseDto;
import com.example.cafehub.dto.user.UserRegistrationRequestDto;
import com.example.cafehub.dto.user.UserResponseDto;
import com.example.cafehub.dto.user.UserWithRoleResponseDto;
import com.example.cafehub.exception.EntityAlreadyExistsException;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.exception.RegistrationException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.mapper.UserMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.UserRepository;
import com.example.cafehub.security.AuthenticationService;
import com.example.cafehub.service.EmailService;
import com.example.cafehub.service.UserService;
import com.example.cafehub.util.CodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final User.Role DEFAULT_ROLE = User.Role.USER;
    private static final Random random = new Random();
    private final AuthenticationService authenticationService;
    @Qualifier("passwordGenerator")
    private final CodeGenerator passwordGenerator;
    @Qualifier("verificationCodeGenerator")
    private final CodeGenerator verificationCodeGenerator;
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
    public UserResponseDto save(UserRegistrationRequestDto requestDto,
                                HttpServletRequest httpRequest) {
        if (findUserByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(String.format("The user with email %s already exists",
                    requestDto.getEmail()));
        }
        User user = userMapper.toModel(requestDto);
        user.setRole(DEFAULT_ROLE);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        String verificationCode = getVerificationCode();
        user.setVerificationCode(verificationCode);
        emailService.sendVerificationEmail(
                requestDto.getEmail(),
                requestDto.getFirstName(),
                verificationCode,
                getSiteUrl(httpRequest)
        );
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto request,
                                      HttpServletRequest httpRequest) {
        Optional<User> userOptional = userRepository.findByEmail(request.email());
        if (userOptional.isPresent() && !userOptional.get().isVerified()) {
            User user = userOptional.get();
            String verificationCode = getVerificationCode();
            user.setVerificationCode(verificationCode);
            userRepository.save(user);
            emailService.sendVerificationEmail(
                    user.getEmail(),
                    user.getFirstName(),
                    user.getVerificationCode(),
                    getSiteUrl(httpRequest)
            );
            throw new AccessDeniedException(
                    "Account hasn't been activated yet. Please verify your email");

        }
        return authenticationService.authenticate(request);
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
            throw new EntityAlreadyExistsException(String.format(
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
            throw new EntityNotFoundException(String.format(
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
        String newPassword = passwordGenerator.generateCode(random.nextInt(5, 10));
        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        emailService.sendPasswordResetEmail(resetDto.email(), newPassword);
    }

    @Override
    public String verifyEmail(String verificationCode) {
        Optional<User> userOptional = userRepository.findByVerificationCode(verificationCode);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setVerified(true);
            userRepository.save(user);
            return String.format("""
                    <!DOCTYPE html>
                    <html lang="en">
                        <head>
                          <title>CafeHub</title>
                        </head>
                        <body style="font-family: Georgia; text-align: center">
                            <h2>Email has been verified</h2>
                            <p>
                                %s, your account is active now.<br>
                                Thank you for choosing CafeHub!
                            </p>
                        </body>
                    </html>
                    """, user.getFirstName());
        }
        return """
                <!DOCTYPE html>
                <html lang="en">
                    <head>
                      <title>CafeHub</title>
                    </head>
                    <body style="font-family: Georgia; text-align: center">
                        <h2>Something went wrong</h2>
                        <p>
                            Please try again later
                        </p>
                    </body>
                </html>
                """;
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

    private String getSiteUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();

        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }
        return url.toString();
    }

    private String getVerificationCode() {
        String verificationCode = null;
        do {
            verificationCode = verificationCodeGenerator.generateCode(random.nextInt(10, 25));
        } while (userRepository.findByVerificationCode(verificationCode).isPresent());
        return verificationCode;
    }
}
