package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestByPatronDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.mapper.UserMapper;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto registerUser(UserSaveRequestDto userSaveRequestDto){

        if (userRepository.existsByUsername(userSaveRequestDto.username())) {
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(userSaveRequestDto.email())) {
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = userMapper.toEntity(userSaveRequestDto);
        user.setActive(true);
        userRepository.save(user);
        return userMapper.toUserDto(user);

    }


    public UserDto registerUserByPatron(UserSaveRequestByPatronDto patronDto){

        if (userRepository.existsByUsername(patronDto.username())) {
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }

        if (userRepository.existsByEmail(patronDto.email())) {
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = userMapper.toEntityWithPatron(patronDto);
        user.setActive(true);
        userRepository.save(user);
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
            throw new InvalidUserIdException(id);
        }
        if(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(),id)){
            throw new IllegalArgumentException("A user with this username already exists. Please create a different username.");
        }
        if (userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(),id)){
            throw new IllegalArgumentException("This email address is already in use by another user");
        }

        User user = userRepository.findUserById(id);
        user.setUsername(userUpdateRequestDto.username());
        user.setEmail(userUpdateRequestDto.email());
        user.setPhone(userUpdateRequestDto.phone());
        user.setName(userUpdateRequestDto.name());
        user.setSurname(userUpdateRequestDto.surname());

        userRepository.save(user);
        return userMapper.toUserDto(user);

    }

    public UserDto updateUserPassword(Long id, UserPasswordUpdateRequestDto passwordUpdateDto){
        if(!userRepository.existsById(id)){
            throw new InvalidUserIdException(id);
        }
        User user = userRepository.findUserById(id);

        if(!passwordUpdateDto.currentPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Incorrect password.");
        }
        if(passwordUpdateDto.newPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Please choose a new password different from your current one.");
        }
        user.setPassword(passwordUpdateDto.newPassword());
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }


    public UserDto updateUserRole(Long id, UserRoleUpdateRequestDto roleUpdateRequestDto){

        User user = userRepository.findById(id).orElseThrow(() -> new InvalidUserIdException(id));
        user.setRole(roleUpdateRequestDto.role());

        userRepository.save(user);
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
        return userMapper.toUserDto(user);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new InvalidUserIdException(id));
        if(user.getActive()){
            throw new IllegalStateException("You cannot delete a user while they are active. Please passive the user before attempting deletion");
        }
        userRepository.delete(user);
    }

}
