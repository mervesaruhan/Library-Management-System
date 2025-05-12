package com.mervesaruhan.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mervesaruhan.librarymanagementsystem.LibraryManagementSystemApplication;
import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.general.UserTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserPasswordUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserRoleUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.updateRequest.UserUpdateRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.enums.RoleEnum;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {LibraryManagementSystemApplication.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(1)
    void shouldCreateUser() throws Exception {
        UserSaveRequestDto request = UserTestDataGenerator.createUserSaveDto();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains(TestConstants.TEST_USER_NAME));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(2)
    void shouldGetUserById() throws Exception {
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(3)
    void shouldGetAllUsers() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(4)
    void shouldUpdateUser() throws Exception {
        Long userId = 1L;
        UserUpdateRequestDto request = UserTestDataGenerator.createUserUpdateDto();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}", userId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(5)
    void shouldUpdateUserPassword() throws Exception {
        Long userId = 1L;
        UserPasswordUpdateRequestDto request = UserTestDataGenerator.createPasswordUpdateDto();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}/password", userId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(6)
    void shouldUpdateUserRole() throws Exception {
        Long userId = 1L;
        UserRoleUpdateRequestDto request = new UserRoleUpdateRequestDto(RoleEnum.ROLE_LIBRARIAN);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}/role", userId)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(7)
    void shouldUpdateUserStatus() throws Exception {
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/users/{id}/status", userId))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(8)
    void shouldDeleteUser() throws Exception {
        Long userId = 1L;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/users/{id}", userId))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(409, status);
    }

    protected boolean isSuccess(MvcResult mvcResult) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();
        RestResponse<?> response = objectMapper.readValue(responseContent, RestResponse.class);
        return response.isSuccess();
    }
}
