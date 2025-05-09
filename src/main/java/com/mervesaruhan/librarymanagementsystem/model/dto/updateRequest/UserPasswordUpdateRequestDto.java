package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateRequestDto(

        @NotBlank(message = "Current password cannot be blank.")
        String currentPassword,

        @NotBlank(message = "New password cannot be blank.")
        @Size(min = 6, message = "New password must be at least 6 characters long.")
        String newPassword

) {}
