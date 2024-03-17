package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.user.UpdateAccountInfoDto;
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
import com.example.cafehub.service.impl.UserServiceImpl;
import com.example.cafehub.util.CodeGenerator;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private User user1;
    private User user2;
    private User user3;
    private UserResponseDto userResponseDto1;
    private UserResponseDto userResponseDto2;
    private UserResponseDto userResponseDto3;
    private Cafe cafe1;
    private Cafe cafe2;
    private CafeResponseDto cafeResponseDto1;
    private CafeResponseDto cafeResponseDto2;
    @Mock
    private CodeGenerator verificationCodeGenerator;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private CafeMapper cafeMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void init() {
        User.Role user = User.Role.USER;

        user1 = new User();
        user1.setId(1L);
        user1.setRole(user);
        user1.setLastName("Curie");
        user1.setFirstName("Marie");
        user1.setEmail("MarieCurie@gmail.com");

        user2 = new User();
        user2.setId(2L);
        user2.setRole(user);
        user2.setLastName("Edison");
        user2.setFirstName("Thomas");
        user2.setEmail("ThomasEdison@gmail.com");

        user3 = new User();
        user3.setId(3L);
        user3.setRole(user);
        user3.setLastName("Tesla");
        user3.setFirstName("Nikola");
        user3.setEmail("NikolaTesla@gmail.com");

        userResponseDto1 = new UserResponseDto();
        userResponseDto1.setId(user1.getId());
        userResponseDto1.setEmail(user1.getEmail());
        userResponseDto1.setLastName(user1.getLastName());
        userResponseDto1.setFirstName(user1.getFirstName());

        userResponseDto2 = new UserResponseDto();
        userResponseDto2.setId(user2.getId());
        userResponseDto2.setEmail(user2.getEmail());
        userResponseDto2.setLastName(user2.getLastName());
        userResponseDto2.setFirstName(user2.getFirstName());

        userResponseDto3 = new UserResponseDto();
        userResponseDto3.setId(user3.getId());
        userResponseDto3.setEmail(user3.getEmail());
        userResponseDto3.setLastName(user3.getLastName());
        userResponseDto3.setFirstName(user3.getFirstName());

        cafe1 = new Cafe();
        cafe1.setId(1L);
        cafe1.setName("Cafe 1");
        cafe1.setAddress("233rd st");
        cafe1.setCity("Kyiv");
        cafe1.setScore(BigDecimal.valueOf(4));

        cafe2 = new Cafe();
        cafe2.setId(2L);
        cafe2.setName("Cafe 2");
        cafe2.setAddress("134th st");
        cafe2.setCity("Lviv");
        cafe2.setScore(BigDecimal.valueOf(3));

        cafeResponseDto1 = new CafeResponseDto();
        cafeResponseDto1.setId(cafe1.getId());
        cafeResponseDto1.setName(cafe1.getName());
        cafeResponseDto1.setScore(cafe1.getScore());
        cafeResponseDto1.setAddress(cafe1.getAddress());

        cafeResponseDto2 = new CafeResponseDto();
        cafeResponseDto2.setId(cafe2.getId());
        cafeResponseDto2.setName(cafe2.getName());
        cafeResponseDto2.setScore(cafe2.getScore());
        cafeResponseDto2.setAddress(cafe2.getAddress());
    }

    @Test
    @DisplayName("Get all users")
    void getAllUsers_ValidRequest_ShouldReturnListOfDtos() {
        List<User> userList = List.of(user1, user2, user3);
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> commentPage = new PageImpl<>(userList, pageable, userList.size());
        Mockito.when(userRepository.findAll(pageable)).thenReturn(commentPage);
        Mockito.when(userMapper.toDto(user1)).thenReturn(userResponseDto1);
        Mockito.when(userMapper.toDto(user2)).thenReturn(userResponseDto2);
        Mockito.when(userMapper.toDto(user3)).thenReturn(userResponseDto3);

        List<UserResponseDto> expected =
                List.of(userResponseDto1, userResponseDto2, userResponseDto3);
        List<UserResponseDto> actual = userService.getAllUsers(pageable);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Save a new user")
    void save_ValidRequest_ShouldReturnDto() {
        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        String testPassword = "password123";
        requestDto.setEmail("test@example.com");
        requestDto.setPassword(testPassword);
        requestDto.setRepeatPassword(testPassword);
        requestDto.setFirstName("John");
        requestDto.setLastName("Johnson");

        User testUser = new User();
        testUser.setId(4L);
        testUser.setEmail(requestDto.getEmail());
        testUser.setPassword("hashedPassword");
        testUser.setLastName(requestDto.getLastName());
        testUser.setFirstName(requestDto.getFirstName());

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(testUser.getId());
        responseDto.setEmail(testUser.getEmail());
        responseDto.setLastName(testUser.getLastName());
        responseDto.setFirstName(testUser.getFirstName());

        Mockito.doNothing().when(emailService).sendVerificationEmail(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString());
        Mockito.when(userRepository.findByEmail(requestDto.getEmail()))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(requestDto.getPassword()))
                .thenReturn(testUser.getPassword());
        Mockito.when(userRepository.findByVerificationCode(null))
                .thenReturn(Optional.empty());
        Mockito.when(userMapper.toModel(requestDto)).thenReturn(testUser);
        Mockito.when(userMapper.toDto(testUser)).thenReturn(responseDto);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(testUser);

        HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);
        UserResponseDto actual = userService.save(requestDto, httpRequest);

        Assertions.assertEquals(responseDto.getId(), actual.getId());
        Assertions.assertEquals(responseDto.getEmail(), actual.getEmail());
        Assertions.assertEquals(responseDto.getFirstName(), actual.getFirstName());
    }

    @Test
    @DisplayName("Save a new user. Email was used before. Throws exception")
    void save_UsedEmail_ShouldThrowException() {
        Mockito.when(userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Optional.of(user1));

        UserRegistrationRequestDto requestDto = new UserRegistrationRequestDto();
        requestDto.setEmail(user1.getEmail());
        HttpServletRequest httpRequest = Mockito.mock(HttpServletRequest.class);

        Assertions.assertThrows(RegistrationException.class,
                () -> userService.save(requestDto, httpRequest));
    }

    @Test
    @DisplayName("Login")
    void login_ValidRequest_ShouldReturnDto() {
        user1.setVerified(true);
        UserLoginRequestDto loginRequestDto =
                new UserLoginRequestDto(user1.getEmail(), user1.getPassword());
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto(
                user1.getFirstName(), user1.getLastName(), user1.getEmail(), "Some-token-123");

        Mockito.when(authenticationService.authenticate(loginRequestDto))
                .thenReturn(userLoginResponseDto);
        Mockito.when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));

        UserLoginResponseDto actual =
                userService.login(loginRequestDto, Mockito.mock(HttpServletRequest.class));

        Assertions.assertEquals(userLoginResponseDto.email(), actual.email());
        Assertions.assertEquals(userLoginResponseDto.token(), actual.token());
        Assertions.assertEquals(userLoginResponseDto.firstName(), actual.firstName());
    }

    @Test
    @DisplayName("Login. Not verified email. Throws exception")
    void login_NotVerifiedEmail_ShouldThrowException() {
        user1.setVerified(false);
        Mockito.when(userRepository.findByEmail(user1.getEmail())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByVerificationCode(null))
                .thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        Mockito.doNothing().when(emailService).sendVerificationEmail(
                Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.anyString());

        UserLoginRequestDto loginRequestDto =
                new UserLoginRequestDto(user1.getEmail(), user1.getPassword());

        Assertions.assertThrows(AccessDeniedException.class,
                () -> userService.login(loginRequestDto, Mockito.mock(HttpServletRequest.class)));
    }

    @Test
    @DisplayName("Update user role")
    void updateRole_ValidRequest_ShouldReturnDto() {
        User.Role admin = User.Role.ADMIN;

        User testUser = new User();
        testUser.setRole(admin);
        testUser.setId(user2.getId());
        testUser.setEmail(user2.getEmail());
        testUser.setLastName(user2.getLastName());
        testUser.setFirstName(user2.getFirstName());

        UserWithRoleResponseDto expected = new UserWithRoleResponseDto();
        expected.setRole(admin.name());
        expected.setId(user2.getId());
        expected.setEmail(user2.getEmail());
        expected.setLastName(user2.getLastName());
        expected.setFirstName(user2.getFirstName());

        Mockito.when(userRepository.findById(expected.getId())).thenReturn(Optional.of(user2));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(userMapper.toDtoWithRole(testUser)).thenReturn(expected);

        UserWithRoleResponseDto actual = userService.updateRole(user2.getId(), admin.name());
        Assertions.assertEquals(expected.getRole(), actual.getRole());
        Assertions.assertEquals(expected.getId(), actual.getId());
    }

    @Test
    @DisplayName("Update user role. Wrong user role. Throws exception")
    void updateRole_WrongRole_ShouldThrowException() {
        Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        String role = "visitor";
        Assertions.assertThrows(RuntimeException.class,
                () -> userService.updateRole(user2.getId(), role));
    }

    @Test
    @DisplayName("Get favorite cafes")
    void getFavorites_ValidRequest_ShouldReturnListOfDtos() {
        Cafe cafe3 = new Cafe();
        cafe3.setId(3L);
        cafe3.setName("Cafe 3");
        cafe3.setAddress("122nd st");
        cafe3.setCity("Kyiv");
        cafe3.setScore(BigDecimal.valueOf(5));

        CafeResponseDto responseDto3 = new CafeResponseDto();
        responseDto3.setId(cafe3.getId());
        responseDto3.setName(cafe3.getName());
        responseDto3.setScore(cafe3.getScore());
        responseDto3.setAddress(cafe3.getAddress());

        List<Cafe> favoriteCafes = List.of(cafe1, cafe2, cafe3);
        user3.setFavorite(favoriteCafes);

        Mockito.when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(cafeResponseDto1);
        Mockito.when(cafeMapper.toDto(cafe2)).thenReturn(cafeResponseDto2);
        Mockito.when(cafeMapper.toDto(cafe3)).thenReturn(responseDto3);

        List<CafeResponseDto> expected = List.of(cafeResponseDto1, cafeResponseDto2, responseDto3);
        List<CafeResponseDto> actual = userService.getFavorites(user3.getId());

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Get favorite cafes. Wrong user id. Throws exception")
    void getFavorites_WrongUserId_ShouldThrowException() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.getFavorites(5L));
    }

    @Test
    @DisplayName("Update account info")
    void updateAccountInfo_ValidRequest_ShouldReturnDto() {
        UpdateAccountInfoDto requestDto = new UpdateAccountInfoDto();
        requestDto.setEmail("newEmail@gmail.com");
        requestDto.setFirstName("NewFirstName");
        requestDto.setLastName("NewLastName");

        User testUser = new User();
        testUser.setId(user1.getId());
        testUser.setRole(user1.getRole());
        testUser.setEmail(requestDto.getEmail());
        testUser.setLastName(requestDto.getLastName());
        testUser.setFirstName(requestDto.getFirstName());

        UserResponseDto expected = new UserResponseDto();
        expected.setId(testUser.getId());
        expected.setEmail(testUser.getEmail());
        expected.setLastName(testUser.getLastName());
        expected.setFirstName(testUser.getFirstName());

        Mockito.when(userMapper.toDto(testUser)).thenReturn(expected);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        UserResponseDto actual = userService.updateAccountInfo(user1.getId(), requestDto);

        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getLastName(), actual.getLastName());
        Assertions.assertEquals(expected.getFirstName(), actual.getFirstName());
    }

    @Test
    @DisplayName("Add favorite cafe")
    void addFavoriteCafe_ValidRequest_ShouldAddCafeToFavorites() {
        user2.setFavorite(new ArrayList<>());

        User testUser = new User();
        testUser.setId(user2.getId());
        testUser.setRole(user2.getRole());
        testUser.setFavorite(List.of(cafe1));
        testUser.setEmail(user2.getEmail());
        testUser.setLastName(user2.getLastName());
        testUser.setFirstName(user2.getFirstName());

        Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));
        Mockito.when(cafeRepository.findById(cafe1.getId())).thenReturn(Optional.of(cafe1));
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(cafeResponseDto1);

        List<CafeResponseDto> expected = List.of(cafeResponseDto1);
        List<CafeResponseDto> actual = userService.addFavoriteCafe(user2.getId(), cafe1.getId());

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Add favorite cafe. Cafe was added before. Throws exception")
    void addFavoriteCafe_AddingAddedCafe_ShouldThrowException() {
        user3.setFavorite(List.of(cafe1, cafe2));
        Mockito.when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));

        Assertions.assertThrows(EntityAlreadyExistsException.class,
                () -> userService.addFavoriteCafe(user3.getId(), cafe1.getId()));
    }

    @Test
    @DisplayName("Add favorite cafe. Wrong cafe id. Throws exception")
    void addFavoriteCafe_WrongCafeId_ShouldThrowException() {
        user3.setFavorite(new ArrayList<>());
        Mockito.when(userRepository.findById(user3.getId())).thenReturn(Optional.of(user3));
        Mockito.when(cafeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.addFavoriteCafe(user3.getId(), cafe1.getId()));
    }

    @Test
    @DisplayName("Remove cafe from favorite")
    void removeFavoriteCafe_ValidRequest_ShouldRemoveCafeFromFavorites() {
        List<Cafe> favorites = new ArrayList<>();
        favorites.add(cafe1);
        favorites.add(cafe2);
        user1.setFavorite(favorites);

        User testUser = new User();
        testUser.setId(user1.getId());
        testUser.setRole(user1.getRole());
        testUser.setEmail(user1.getEmail());
        testUser.setFavorite(List.of(cafe1));
        testUser.setLastName(user1.getLastName());
        testUser.setFirstName(user1.getFirstName());

        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(cafeResponseDto1);
        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        List<CafeResponseDto> actual = userService.removeFavoriteCafe(user1.getId(), cafe2.getId());

        Assertions.assertEquals(testUser.getFavorite().size(), actual.size());
        Assertions.assertEquals(cafeResponseDto1.getId(), actual.get(0).getId());
        Assertions.assertEquals(cafeResponseDto1.getName(), actual.get(0).getName());
    }

    @Test
    @DisplayName("Remove cafe from favorite. Cafe was removed before. Throws exception")
    void removeFavoriteCafe_RemovedCafeBefore_ShouldThrowException() {
        user2.setFavorite(List.of(cafe2));

        Mockito.when(userRepository.findById(user2.getId())).thenReturn(Optional.of(user2));

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> userService.removeFavoriteCafe(user2.getId(), cafe1.getId()));
    }
}
