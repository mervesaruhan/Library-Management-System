package com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(

        @NotBlank(message = "İsim boş olamaz.")
        @Size(min = 2, max = 50, message = "İsim en az 2, en fazla 50 karakter olmalıdır.")
        String name,

        @NotBlank(message = "Soyisim boş olamaz.")
        @Size(min = 2, max = 50, message = "Soyisim en az 2, en fazla 50 karakter olmalıdır.")
        String surname,

        @NotBlank(message = "Email boş olamaz.")
        @Email(message = "Geçerli bir email adresi giriniz.")
        String email,

        @NotBlank(message = "Telefon numarası boş olamaz.")
        @Pattern(regexp = "^[0-9]{10,15}$", message = "Telefon numarası yalnızca rakamlardan oluşmalı ve 10-15 karakter olmalıdır.")
        String phone,

        @NotBlank(message = "Kullanıcı adı boş olamaz.")
        @Size(min = 4, max = 20, message = "Kullanıcı adı 4-20 karakter arasında olmalıdır.")
        String username

) {}