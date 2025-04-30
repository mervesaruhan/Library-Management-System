package com.mervesaruhan.librarymanagementsystem.model.dto.response;

import com.mervesaruhan.librarymanagementsystem.model.entity.Borrowing;
import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;

import java.util.List;

public record UserDto(
        Long id,
        String fullName,
        String email,
        String username,
        RoleEnum role,
        Boolean active,
        List<BorrowedBookDto> borrowedList
){}
