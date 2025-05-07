package com.mervesaruhan.librarymanagementsystem.service.authentication;

import com.mervesaruhan.librarymanagementsystem.controller.UserController;
import com.mervesaruhan.librarymanagementsystem.model.exception.customizedException.InvalidUserIdException;
import com.mervesaruhan.librarymanagementsystem.model.exception.GlobalExceptionHandler;
import com.mervesaruhan.librarymanagementsystem.service.UserService;
import com.mervesaruhan.librarymanagementsystem.util.LogHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import({MockSecurityConfig.class, GlobalExceptionHandler.class})
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private LogHelper logHelper;

    // LIBRARIAN pasif kullanıcıyı silebilmeli
    @Test
    @WithMockUser(username = "librarian", roles = {"LIBRARIAN"})
    void librarianCanDeletePassiveUser() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    // PATRON silme işlemine erişememeli
    @Test
    @WithMockUser(username = "patron", roles = {"PATRON"})
    void patronCannotDeleteUser() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isForbidden());
    }

    // LIBRARIAN aktif kullanıcı silmeye çalışırsa 409 almalı
    @Test
    @WithMockUser(username = "librarian", roles = {"LIBRARIAN"})
    void cannotDeleteActiveUser_shouldReturnConflict() throws Exception {
        doThrow(new IllegalStateException("You cannot delete a user while they are active."))
                .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isConflict());
    }

    // Geçersiz ID için 404
    @Test
    @WithMockUser(username = "librarian", roles = {"LIBRARIAN"})
    void deleteUser_invalidId_shouldReturnNotFound() throws Exception {
        doThrow(new InvalidUserIdException(999L))
                .when(userService).deleteUser(999L);

        mockMvc.perform(delete("/users/999"))
                .andExpect(status().isNotFound());
    }
}

