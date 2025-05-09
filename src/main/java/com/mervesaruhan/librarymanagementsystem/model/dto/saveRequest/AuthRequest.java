package com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank(message = "Username cannot be blank.")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters.")
        String username,
        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 6, message = "Password must be at least 6 characters.")
        String password) {
}
