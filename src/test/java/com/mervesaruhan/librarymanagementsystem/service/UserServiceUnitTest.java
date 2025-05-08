package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.general.UserTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.UserMapper;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private LogHelper logHelper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User userPatron;
    private User userLibrarian;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userPatron = UserTestDataGenerator.createUserPatron();
        userLibrarian = UserTestDataGenerator.createUserLibrarian();
        userDto = UserTestDataGenerator.createUserDto();

    }

    @Test
    void shouldRegisterUserSuccessfully() {
        UserSaveRequestDto userSaveRequestDto = UserTestDataGenerator.createUserSaveDto();

        when(userRepository.existsByUsername(userSaveRequestDto.username())).thenReturn(false);
        when(userRepository.existsByEmail(userSaveRequestDto.email())).thenReturn(false);
        when(userMapper.toEntity(userSaveRequestDto)).thenReturn(userPatron);
        when(userRepository.save(userPatron)).thenReturn(userPatron);
        when(userMapper.toUserDto(userPatron)).thenReturn(userDto);

        UserDto result = userService.registerUserByLibrarian(userSaveRequestDto);

        assertThat(result).isEqualTo(userDto);
        verify(userRepository).save(userPatron);
        verify(userMapper).toUserDto(userPatron);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        UserSaveRequestDto userSaveRequestDto = UserTestDataGenerator.createUserSaveDto();
        when(userRepository.existsByUsername(userSaveRequestDto.username())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUserByLibrarian(userSaveRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("A user with this username already exists");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserSaveRequestDto userSaveRequestDto = UserTestDataGenerator.createUserSaveDto();

        when(userRepository.existsByUsername(userSaveRequestDto.username())).thenReturn(false);
        when(userRepository.existsByEmail(userSaveRequestDto.email())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUserByLibrarian(userSaveRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This email address is already in use by another user");
    }



    @Test
    void shouldReturnUserByIdSuccessfully() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userPatron));
        when(userMapper.toUserDto(userPatron)).thenReturn(userDto);

        UserDto result = userService.getUserById(userId);

        assertThat(result).isEqualTo(userDto);
        verify(userRepository).findById(userId);
        verify(userMapper).toUserDto(userPatron);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("User not found with id: " + userId);
    }

    @Test
    void shouldReturnAllUsers() {
        Pageable pageable = Pageable.unpaged();
        Page<User> userPage = new PageImpl<>(List.of(userPatron, userLibrarian));

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        Page<UserDto> result = userService.findAllUsers(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent()).containsExactly(userDto, userDto);
        verify(userRepository).findAll(pageable);
        verify(userMapper).toUserDto(userPatron);
        verify(userMapper).toUserDto(userLibrarian);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        Long userId = TestConstants.TEST_USER_ID;
        UserUpdateRequestDto userUpdateRequestDto = UserTestDataGenerator.createUserUpdateDto();
        UserDto updatedUserDto = UserTestDataGenerator.createUpdatedUserDto();
        User updatedUser = UserTestDataGenerator.createUpdatedUserLibrarian();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(), userId)).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(), userId)).thenReturn(false);
        when(userRepository.findUserById(userId)).thenReturn(updatedUser);
        when(userMapper.toUserDto(any(User.class))).thenReturn(updatedUserDto);

        UserDto result = userService.updateUser(userId, userUpdateRequestDto);

        assertThat(result).isEqualTo(updatedUserDto);
        verify(userRepository).save(updatedUser);
        verify(userMapper).toUserDto(updatedUser);
    }

    /// //////
    /// //////
    /// //////
    /// //////
    /// //////
    /// //////
    @Test
    void shouldUpdateUserSuccessfully2() {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = UserTestDataGenerator.createUserUpdateDto();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(), userId)).thenReturn(false);
        when(userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(), userId)).thenReturn(false);
        when(userRepository.findUserById(userId)).thenReturn(userLibrarian);
        when(userRepository.save(userLibrarian)).thenReturn(userLibrarian);
        when(userMapper.toUserDto(userLibrarian)).thenReturn(userDto);

        UserDto result = userService.updateUser(userId, userUpdateRequestDto);

        assertThat(result).isEqualTo(userDto);
        verify(userRepository).save(userLibrarian);
        verify(userMapper).toUserDto(userLibrarian);
    }

    @Test
    void shouldThrowExceptionWhenUserIdNotFoundForUpdate() {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto("new_username", "new_email@example.com", "123456789", "John", "Doe");

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUser(userId, userUpdateRequestDto))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("User not found with id: " + userId);
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExistsForUpdate() {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto("existing_username", "new_email@example.com", "123456789", "John", "Doe");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(), userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userId, userUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("A user with this username already exists");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExistsForUpdate() {
        Long userId = 1L;
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto("new_username", "existing_email@example.com", "123456789", "John", "Doe");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(), userId)).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(userId, userUpdateRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("This email address is already in use by another user");
    }

    @Test
    void shouldUpdateUserPasswordSuccessfully() {
        Long userId = 1L;
        UserPasswordUpdateRequestDto passwordUpdateDto = UserTestDataGenerator.createPasswordUpdateDto();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findUserById(userId)).thenReturn(userLibrarian);
        when(userRepository.save(userLibrarian)).thenReturn(userLibrarian);
        when(userMapper.toUserDto(userLibrarian)).thenReturn(userDto);

        UserDto result = userService.updateUserPassword(userId, passwordUpdateDto);

        assertThat(result).isEqualTo(userDto);
        verify(userRepository).save(userLibrarian);
        verify(userMapper).toUserDto(userLibrarian);
    }

    @Test
    void shouldThrowExceptionWhenIncorrectPasswordForUpdate() {
        Long userId = 1L;
        UserPasswordUpdateRequestDto passwordUpdateDto = new UserPasswordUpdateRequestDto("wrongpassword", "newpassword456");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findUserById(userId)).thenReturn(userLibrarian);

        assertThatThrownBy(() -> userService.updateUserPassword(userId, passwordUpdateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Incorrect password");
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordIsSameAsOldForUpdate() {
        Long userId = TestConstants.TEST_USER_ID;
        String currentPassword = TestConstants.TEST_USER_PASSWORD;
        UserPasswordUpdateRequestDto passwordUpdateDto = new UserPasswordUpdateRequestDto(currentPassword, currentPassword);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findUserById(userId)).thenReturn(userLibrarian);

        assertThatThrownBy(() -> userService.updateUserPassword(userId, passwordUpdateDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Please choose a new password different from your current one.");
    }

    @Test
    void shouldUpdateUserRoleSuccessfully() {
        Long userId = 1L;
        UserRoleUpdateRequestDto roleUpdateRequestDto = UserTestDataGenerator.createRoleUpdateDtoLibrarian();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userPatron));
        when(userRepository.save(any(User.class))).thenReturn(userLibrarian);
        when(userMapper.toUserDto(any(User.class))).thenReturn(userDto);

        UserDto result = userService.updateUserRole(userId, roleUpdateRequestDto);

        assertThat(result).isEqualTo(userDto);
        verify(userMapper).toUserDto(any(User.class));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForRoleUpdate() {
        Long userId = 1L;
        UserRoleUpdateRequestDto roleUpdateRequestDto = UserTestDataGenerator.createRoleUpdateDtoLibrarian();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.updateUserRole(userId, roleUpdateRequestDto))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("User not found with id: " + userId);
    }

    @Test
    void shouldUpdateActiveStatusWhenNoEligibilityViolation() {
        Long userId = TestConstants.TEST_USER_ID;
        Boolean newStatus = true;

        User user = UserTestDataGenerator.createUserLibrarian();
        user.setActive(false);
        user.setBorrowedList(List.of()); // aktif ödünç yok

        UserDto expectedDto = UserTestDataGenerator.createUserDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expectedDto);

        UserDto result = userService.checkUserEligibilityAndUpdateStatus(userId, newStatus);

        assertThat(result).isEqualTo(expectedDto);
        assertThat(user.getActive()).isTrue();
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }


    @Test
    void shouldSetUserInactiveWhenMoreThanFiveBooks() {
        Long userId = TestConstants.TEST_USER_ID;

        User user = UserTestDataGenerator.createUserLibrarian();
        user.setActive(true);

        List<Borrowing> borrowings = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Borrowing borrowing = new Borrowing();
            borrowing.setReturnDate(null); // hala iade edilmemiş
            borrowing.setDueDate(LocalDate.now().plusDays(3)); // gecikmiş değil
            borrowings.add(borrowing);
        }
        user.setBorrowedList(borrowings);

        UserDto expectedDto = UserTestDataGenerator.createUserDto();
        expectedDto = new UserDto(
                user.getId(),
                expectedDto.fullName(),
                expectedDto.email(),
                expectedDto.username(),
                expectedDto.role(),
                false, // otomatik pasifleştirme sonucu
                expectedDto.borrowedList()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expectedDto);

        UserDto result = userService.checkUserEligibilityAndUpdateStatus(userId, true);

        assertThat(user.getActive()).isFalse();
        assertThat(result.active()).isFalse();
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }


    @Test
    void shouldSetUserInactiveWhenOverdueExists() {
        Long userId = TestConstants.TEST_USER_ID;

        User user = UserTestDataGenerator.createUserLibrarian();
        user.setActive(true);

        Borrowing overdueBorrowing = new Borrowing();
        overdueBorrowing.setReturnDate(null);
        overdueBorrowing.setDueDate(LocalDate.now().minusDays(2)); // gecikmiş

        user.setBorrowedList(List.of(overdueBorrowing));

        UserDto expectedDto = UserTestDataGenerator.createUserDto();
        expectedDto = new UserDto(
                user.getId(),
                expectedDto.fullName(),
                expectedDto.email(),
                expectedDto.username(),
                expectedDto.role(),
                false, // gecikmeden dolayı otomatik pasif
                expectedDto.borrowedList()
        );


        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(expectedDto);

        UserDto result = userService.checkUserEligibilityAndUpdateStatus(userId, true);

        assertThat(user.getActive()).isFalse();
        assertThat(result.active()).isFalse();
        verify(userRepository).save(user);
        verify(userMapper).toUserDto(user);
    }

    @Test
    void shouldThrowExceptionWhenTryingToDeleteActiveUser() {
        // Arrange
        Long userId = 1L;
        userLibrarian.setActive(true);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userLibrarian));

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("You cannot delete a user while they are active");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        // Arrange
        Long userId = 1L;
        userLibrarian.setActive(false);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(userLibrarian));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).delete(userLibrarian);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForDelete() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(InvalidUserIdException.class)
                .hasMessageContaining("User not found with id: " + userId);
    }
}
