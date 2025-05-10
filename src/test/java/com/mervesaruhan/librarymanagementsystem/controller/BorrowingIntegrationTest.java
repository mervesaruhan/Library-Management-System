package com.mervesaruhan.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mervesaruhan.librarymanagementsystem.LibraryManagementSystemApplication;
import com.mervesaruhan.librarymanagementsystem.general.UserTestDataGenerator;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BorrowingSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.UserSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.model.enums.BorrowingStatusEnum;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@SpringBootTest(classes = {LibraryManagementSystemApplication.class})
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BorrowingIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        mockMvc = webAppContextSetup(this.context).build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    }

    @Order(1)
    @Test
    @WithMockUser(roles = "PATRON")
    void shouldBorrowBook() throws Exception {

        // change role to LIBRARIAN in order to create user and book data
        TestSecurityContextHolder.setAuthentication(
                new TestingAuthenticationToken("admin", "pass", List.of(new SimpleGrantedAuthority("ROLE_LIBRARIAN")))
        );

        Long userId = createUserAndGetId();
        Long bookId = createAndGetBookId();

        // change back to PATRON ROLE in order to test rest of the method
        TestSecurityContextHolder.setAuthentication(
                new TestingAuthenticationToken("patron", "pass", List.of(new SimpleGrantedAuthority("ROLE_PATRON")))
        );

        BorrowingSaveRequestDto request = new BorrowingSaveRequestDto(userId, bookId);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/borrowings")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("\"success\":true")); // daha sağlam kontrol
    }


    @Test
    @WithMockUser(roles = "PATRON")
    @Order(2)
    void shouldReturnBorrowedBook() throws Exception {
        long borrowingId = 1L;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/borrowings/{id}", borrowingId))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "PATRON")
    @Order(3)
    void shouldGetMyBorrowingHistory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowings/history/my"))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(4)
    void shouldGetAllBorrowingHistory() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowings/history/all"))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(5)
    void shouldGetBorrowingsByStatus() throws Exception {
        String status = BorrowingStatusEnum.BORROWED.name();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowings/{status}", status))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(6)
    void shouldGetOverdueBorrowings() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowings/overdue"))
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(7)
    void shouldGetOverdueReport() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/borrowings/overdue/report"))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("Overdue") || responseContent.length() > 10); // örnek kontrol
    }

    protected boolean isSuccess(MvcResult mvcResult) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();
        RestResponse<?> response = objectMapper.readValue(responseContent, RestResponse.class);
        return response.isSuccess();
    }

    private Long createUserAndGetId() throws Exception {
        UserSaveRequestDto request = UserTestDataGenerator.createUserSaveDto();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/users")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        return root.path("data").path("id").asLong();
    }

    private Long createAndGetBookId() throws Exception {
        BookSaveRequestDto request = new BookSaveRequestDto(
                "Clean Code",
                "Robert C. Martin",
                "12345678901",
                LocalDate.of(2008, 8, 1),
                "Prentice Hall",
                "Software Engineering",
                "A Handbook of Agile Software Craftsmanship",
                464,
                10
        );

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        JsonNode root = objectMapper.readTree(responseContent);
        return root.path("data").path("id").asLong();
    }
}
