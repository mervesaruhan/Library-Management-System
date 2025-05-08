package com.mervesaruhan.librarymanagementsystem.general;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;

import static com.mervesaruhan.librarymanagementsystem.general.BookTestDataGenerator.createBorrowedBookDtoList;

public class UserTestDataGenerator {

    public static UserDto createUserDto() {
        return new UserDto(
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_USER_FULLNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_ROLE_LIBRARIAN,
                TestConstants.TEST_IS_ACTIVE,
                createBorrowedBookDtoList()
        );
    }

    public static UserDto createUpdatedUserDto() {
        return new UserDto(
                TestConstants.TEST_USER_ID,
                TestConstants.TEST_UPDATED_USER_FULLNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_ROLE_LIBRARIAN,
                TestConstants.TEST_IS_ACTIVE,
                createBorrowedBookDtoList()
        );
    }

    public static UserSaveRequestDto createUserSaveDto() {
        return new UserSaveRequestDto(
                TestConstants.TEST_USER_NAME,
                TestConstants.TEST_USER_SURNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_PHONE,
                TestConstants.TEST_USER_USERNAME,
                TestConstants.TEST_USER_PASSWORD,
                TestConstants.TEST_ROLE_LIBRARIAN
        );
    }



    public static UserUpdateRequestDto createUserUpdateDto() {
        return new UserUpdateRequestDto(
                TestConstants.TEST_UPDATED_USER_NAME,
                TestConstants.TEST_USER_SURNAME,
                TestConstants.TEST_USER_EMAIL,
                TestConstants.TEST_USER_PHONE,
                TestConstants.TEST_USER_USERNAME
        );
    }

    public static UserRoleUpdateRequestDto createRoleUpdateDtoLibrarian() {
        return new UserRoleUpdateRequestDto(
                TestConstants.TEST_ROLE_LIBRARIAN
        );
    }

    public static UserPasswordUpdateRequestDto createPasswordUpdateDto() {
        return new UserPasswordUpdateRequestDto(
                TestConstants.TEST_USER_PASSWORD,
                TestConstants.TEST_USER_PASSWORD_NEW
        );
    }

    public static User createUserLibrarian() {
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setName(TestConstants.TEST_USER_NAME);
        user.setSurname(TestConstants.TEST_USER_SURNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPhone(TestConstants.TEST_USER_PHONE);
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRole(TestConstants.TEST_ROLE_LIBRARIAN);
        user.setActive(TestConstants.TEST_IS_ACTIVE);
        return user;
    }

    public static User createUpdatedUserLibrarian() {
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setName(TestConstants.TEST_UPDATED_USER_NAME);
        user.setSurname(TestConstants.TEST_USER_SURNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPhone(TestConstants.TEST_USER_PHONE);
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRole(TestConstants.TEST_ROLE_LIBRARIAN);
        user.setActive(TestConstants.TEST_IS_ACTIVE);
        return user;
    }

    public static User createUserPatron() {
        User user = new User();
        user.setId(TestConstants.TEST_USER_ID);
        user.setName(TestConstants.TEST_USER_NAME);
        user.setSurname(TestConstants.TEST_USER_SURNAME);
        user.setEmail(TestConstants.TEST_USER_EMAIL);
        user.setPhone(TestConstants.TEST_USER_PHONE);
        user.setUsername(TestConstants.TEST_USER_USERNAME);
        user.setPassword(TestConstants.TEST_USER_PASSWORD);
        user.setRole(TestConstants.TEST_ROLE_PATRON);
        user.setActive(TestConstants.TEST_IS_ACTIVE);
        return user;
    }

}
