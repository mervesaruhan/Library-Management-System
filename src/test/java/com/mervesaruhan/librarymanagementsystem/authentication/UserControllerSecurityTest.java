package com.mervesaruhan.librarymanagementsystem.authentication;

import com.mervesaruhan.librarymanagementsystem.controller.UserController;
import com.mervesaruhan.librarymanagementsystem.model.exception.GeneralControllerAdvice;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.service.UserService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({MockSecurityConfig.class, GeneralControllerAdvice.class,ExtendedMockConfig.class, TestMethodSecurityConfig.class})
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;

    @Autowired
    private LogHelper logHelper;



    // LIBRARIAN pasif kullanıcıyı silebilmeli
    @Test
    void librarianCanDeletePassiveUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1")
                        .with(user("librarian").roles("LIBRARIAN"))
                        .with(csrf()))
                .andExpect(status().isOk());
    }


    // PATRON silme işlemine erişememeli
    @Test
    @WithMockUser(username = "patron", roles = {"PATRON"})
    void patronCannotDeleteUser() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andExpect(status().isForbidden());
    }


    // LIBRARIAN aktif kullanıcı silmeye çalışırsa 409 almalı
    @Test
    void cannotDeleteActiveUser_shouldReturnConflict() throws Exception {
        Mockito.doThrow(new IllegalStateException("You cannot delete a user while they are active. Please passive the user before attempting deletion"))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/v1/users/1")
                        .with(user("librarian").roles("LIBRARIAN"))
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    // Geçersiz ID için 404
    @Test
    void deleteUser_invalidId_shouldReturnNotFound() throws Exception {
        Mockito.doThrow(new InvalidUserIdException())
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/api/v1/users/999")
                        .with(user("librarian").roles("LIBRARIAN"))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}

