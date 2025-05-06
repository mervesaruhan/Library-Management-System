package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestByPatronDto;
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
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LogHelper logHelper;

    public UserDto registerUser(UserSaveRequestDto userSaveRequestDto){

        if (userRepository.existsByUsername(userSaveRequestDto.username())) {
            logHelper.warn("Username already exists: {}", userSaveRequestDto.username());
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(userSaveRequestDto.email())) {
            logHelper.warn("Email already exists: {}", userSaveRequestDto.email());
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = userMapper.toEntity(userSaveRequestDto);
        user.setActive(true);
        userRepository.save(user);
        logHelper.info("User registered successfully. ID: {}, username: {}", user.getId(), user.getUsername());
        return userMapper.toUserDto(user);

    }


    public UserDto registerUserByPatron(UserSaveRequestByPatronDto patronDto){

        if (userRepository.existsByUsername(patronDto.username())) {
            logHelper.warn("Username already exists for patron: {}", patronDto.username());
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(patronDto.email())) {
            logHelper.warn("Email already exists for patron: {}", patronDto.email());
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = userMapper.toEntityWithPatron(patronDto);
        user.setActive(true);
        userRepository.save(user);

        logHelper.info("Patron user registered successfully. ID: {}, username: {}", user.getId(), user.getUsername());
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

        User user = userRepository.findUserById(id);
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
        User user = userRepository.findUserById(id);

        if(!passwordUpdateDto.currentPassword().equals(user.getPassword())){
            logHelper.warn("Incorrect current password for user ID: {}", id);
            throw new IllegalArgumentException("Incorrect password.");
        }
        if(passwordUpdateDto.newPassword().equals(user.getPassword())){
            logHelper.warn("New password matches current password. ID: {}", id);
            throw new IllegalArgumentException("Please choose a new password different from your current one.");
        }
        user.setPassword(passwordUpdateDto.newPassword());
        userRepository.save(user);
        logHelper.info("Password updated successfully for user ID: {}", id);
        return userMapper.toUserDto(user);
    }


    public UserDto updateUserRole(Long id, UserRoleUpdateRequestDto roleUpdateRequestDto){

        User user = userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException(id));
        user.setRole(roleUpdateRequestDto.role());

        userRepository.save(user);
        logHelper.info("User role updated. ID: {}, new role: {}", id, roleUpdateRequestDto.role());
        return userMapper.toUserDto(user);
    }

    public UserDto updateUserActiveStatus(Long id, Boolean active){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserIdException(id));
//        if (user.getRole()== RoleEnum.LIBRARIAN && !active){
//            throw  new IllegalArgumentException("LIBRARIAN cannot be passive");
//        }
        user.setActive(active);

        userRepository.save(user);
        logHelper.info("User active status updated. ID: {}, active: {}", id, active);
        return userMapper.toUserDto(user);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserIdException(id));
        if(user.getActive()){
            logHelper.warn("Attempted to delete active user. ID: {}", id);
            throw new IllegalStateException("You cannot delete a user while they are active. Please passive the user before attempting deletion");
        }
        userRepository.delete(user);
        logHelper.info("User deleted successfully. ID: {}", id);
    }

}
