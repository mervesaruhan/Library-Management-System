package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(

        @NotBlank(message = "First name cannot be blank.")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters.")
        String name,

        @NotBlank(message = "Last name cannot be blank.")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters.")
        String surname,

        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Please enter a valid email address.")
        String email,

        @NotBlank(message = "Phone number cannot be blank.")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must consist of digits only and be between 10 and 15 characters.")
        String phone,

        @NotBlank(message = "Username cannot be blank.")
        @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters..")
        String username

) {}