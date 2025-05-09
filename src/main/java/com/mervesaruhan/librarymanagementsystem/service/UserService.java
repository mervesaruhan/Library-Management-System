package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.UserMapper;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogHelper logHelper;
    private final PasswordEncoder passwordEncoder;

    public UserDto registerUserByLibrarian(UserSaveRequestDto userSaveRequestDto){

        if (userRepository.existsByUsername(userSaveRequestDto.username())) {
            logHelper.warn("Username already exists: {}", userSaveRequestDto.username());
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(userSaveRequestDto.email())) {
            logHelper.warn("Email already exists: {}", userSaveRequestDto.email());
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        final User user = userMapper.toEntity(userSaveRequestDto);
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        logHelper.info("User registered successfully. ID: {}, username: {}", user.getId(), user.getUsername());
        return userMapper.toUserDto(user);

    }

    public UserDto getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new InvalidUserIdException(id));
    }

    public Page<UserDto> findAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toUserDto);
    }

    public UserDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto){

        if(!userRepository.existsById(id)){
            logHelper.warn("Attempted to update non-existent user. ID: {}", id);
            throw new InvalidUserIdException(id);
        }
        if(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(),id)){
            logHelper.warn("Username conflict during update. ID: {}, username: {}", id, userUpdateRequestDto.username());
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }
        if (userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(),id)){
            logHelper.warn("Email conflict during update. ID: {}, email: {}", id, userUpdateRequestDto.email());
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        final User user = userRepository.findUserById(id);
        user.setUsername(userUpdateRequestDto.username());
        user.setEmail(userUpdateRequestDto.email());
        user.setPhone(userUpdateRequestDto.phone());
        user.setName(userUpdateRequestDto.name());
        user.setSurname(userUpdateRequestDto.surname());
        userRepository.save(user);

        logHelper.info("User updated successfully. ID: {}", id);
        return userMapper.toUserDto(user);
    }

    public UserDto updateUserPassword(Long id, UserPasswordUpdateRequestDto passwordUpdateDto){
        if(!userRepository.existsById(id)){
            logHelper.warn("Attempted password update for non-existent user. ID: {}", id);
            throw new InvalidUserIdException(id);
        }
        final User user = userRepository.findUserById(id);

        if(!passwordUpdateDto.currentPassword().equals(user.getPassword())){
            logHelper.warn("Incorrect current password for user ID: {}", id);
            throw new IllegalArgumentException("Incorrect password.");
        }
        if(passwordUpdateDto.newPassword().equals(user.getPassword())){
            logHelper.warn("New password matches current password. ID: {}", id);
            throw new IllegalArgumentException("Please choose a new password different from your current one.");
        }

        user.setPassword(passwordEncoder.encode(passwordUpdateDto.newPassword()));
        userRepository.save(user);
        logHelper.info("Password updated successfully for user ID: {}", id);
        return userMapper.toUserDto(user);
    }

    public UserDto updateUserRole(Long id, UserRoleUpdateRequestDto roleUpdateRequestDto){

        final User user = userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException(id));
        user.setRole(roleUpdateRequestDto.role());

        userRepository.save(user);
        logHelper.info("User role updated. ID: {}, new role: {}", id, roleUpdateRequestDto.role());
        return userMapper.toUserDto(user);
    }

    public boolean isUserEligible(Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserIdException(userId));

        final List<Borrowing> activeBorrowings = user.getBorrowedList().stream()
                .filter(b -> b.getReturnDate() == null)
                .toList();

        final boolean hasMoreThanFiveBooks = activeBorrowings.size() >= 5;
        final boolean hasOverdue = activeBorrowings.stream()
                .anyMatch(b -> b.getDueDate().isBefore(LocalDate.now()));

        return !(hasMoreThanFiveBooks || hasOverdue);
    }

    public UserDto checkUserEligibilityAndUpdateStatus(Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidUserIdException(userId));

        final boolean expectedStatus = isUserEligible(userId);

        if (expectedStatus != user.getActive()) {
            user.setActive(expectedStatus);
            userRepository.save(user);
            final String status = expectedStatus ? "active" : "passive";
            logHelper.info("User {} status updated to {}", userId, status);
        } else {
            logHelper.debug("User {} status already correct.", userId);
        }

        return userMapper.toUserDto(user);
    }

    public void deleteUser(Long id){
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserIdException(id));
        if(user.getActive()){
            logHelper.warn("Attempted to delete active user. ID: {}", id);
            throw new IllegalStateException("You cannot delete a user while they are active. Please passive the user before attempting deletion");
        }
        userRepository.delete(user);
        logHelper.info("User deleted successfully. ID: {}", id);
    }
}
