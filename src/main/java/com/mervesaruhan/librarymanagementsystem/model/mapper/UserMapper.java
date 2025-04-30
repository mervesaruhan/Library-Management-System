package com.mervesaruhan.librarymanagementsystem.model.mapper;

import com.mervesaruhan.librarymanagementsystem.model.dto.response.BorrowedBookDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.response.UserDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "fullName", expression = "java(user.getName() + \" \" + user.getSurname())")
    @Mapping(target = "borrowedBooks", source = "borrowingList")
    UserDto toUserDto(User user);

    User toEntity(UserSaveRequestDto userSaveRequestDto);

    List<UserDto> toUserDtoList(List<User> users);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "author", source = "book.author")
    BorrowedBookDto toBorrowedBookDto(Borrowing borrowing);

    List<BorrowedBookDto> toBorrowedBookDtoList(List<Borrowing> borrowings);
}