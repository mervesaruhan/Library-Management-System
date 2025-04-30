package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateRequestDto(

        @NotNull(message = "Rol boş olamaz.")
        RoleEnum role
) {}