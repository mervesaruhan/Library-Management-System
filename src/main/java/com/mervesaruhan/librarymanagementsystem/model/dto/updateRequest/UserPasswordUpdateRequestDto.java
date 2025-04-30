package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordUpdateRequestDto(

        @NotBlank(message = "Mevcut şifre boş olamaz.")
        String currentPassword,

        @NotBlank(message = "Yeni şifre boş olamaz.")
        @Size(min = 6, message = "Yeni şifre en az 6 karakter olmalıdır.")
        String newPassword

) {}
