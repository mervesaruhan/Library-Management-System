package com.mervesaruhan.librarymanagementsystem.service;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import com.mervesaruhan.librarymanagementsystem.model.mapper.UserMapper;
import com.mervesaruhan.librarymanagementsystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
            throw new IllegalArgumentException("Bu isimde başka bir kullanıcı mevcut. Başka bir kullanıcı adı oluştrunuz.");
        }

        if (userRepository.existsByEmail(userSaveRequestDto.email())) {
            throw new IllegalArgumentException("Bu mail adresi başka bir kullanıcı tarafından kullanılıyor.");
        }

        User user = userMapper.toEntity(userSaveRequestDto);
        user.setActive(true);
        userRepository.save(user);
        return userMapper.toUserDto(user);

    }


    public UserDto getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new EntityNotFoundException("ID " + id + " ile kullanıcı bulunamadı."));
    }

    public Page<UserDto> findAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(userMapper::toUserDto);
    }

    public UserDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto){

        if(!userRepository.existsById(id)){
            throw new EntityNotFoundException("Girilen ID'de kullanıcı bulunamamıştır. ID: " + id );
        }
        if(userRepository.existsByUsernameAndIdNot(userUpdateRequestDto.username(),id)){
            throw new IllegalArgumentException("Bu isimde başka bir kullanıcı mevcut. Başka bir kullanıcı adı oluştrunuz.");
        }
        if (userRepository.existsByEmailAndIdNot(userUpdateRequestDto.email(),id)){
            throw new IllegalArgumentException("Bu mail adresi başka bir kullanıcı tarafından kullanılıyor.");
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
            throw new EntityNotFoundException("Girilen ID'de kullanıcı bulunamamıştır. ID: " + id );
        }
        User user = userRepository.findUserById(id);

        if(!passwordUpdateDto.currentPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Yanlış şifre girdiniz");
        }
        if(passwordUpdateDto.newPassword().equals(user.getPassword())){
            throw new IllegalArgumentException("Yeni Şifre mevcut şifreniz ile aynı olamaz.");
        }
        user.setPassword(passwordUpdateDto.newPassword());
        userRepository.save(user);
        return userMapper.toUserDto(user);
    }


    public UserDto updateUserRole(Long id, UserRoleUpdateRequestDto roleUpdateRequestDto){

        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("ID " + id + " ile kullanıcı bulunamadı."));
        user.setRole(roleUpdateRequestDto.role());

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public UserDto updateUserActiveStatus(Long id, Boolean active){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("ID " + id + " ile kullanıcı bulunamadı."));
        user.setActive(active);

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("ID " + id + " ile kullanıcı bulunamadı."));
        if(user.getActive()){
            throw new IllegalStateException("Aktif kullanıcı silinemez. Önce pasifleştirin.");
        }
        userRepository.delete(user);
    }

}
