package com.mervesaruhan.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mervesaruhan.librarymanagementsystem.LibraryManagementSystemApplication;
import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {LibraryManagementSystemApplication.class})
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookIntegrationTest {

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
    void shouldSaveBook() throws Exception {
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
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        assertTrue(responseContent.contains("Clean Code"));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(2)
    void shouldGetAllBooks() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        assertTrue(json.contains("content")); // or test more fields
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(3)
    void shouldGetBookById() throws Exception {
        Long bookId = TestConstants.TEST_BOOK_ID;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)

                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "PATRON")
    @Order(4)
    void shouldSearchBooks() throws Exception {
        String keyword = "Code";
        String field = "TITLE"; // Enum olduğundan kullanılabilir bir değer verilmeli

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/search/filter")
                        .param("keyword", keyword)
                        .param("field", field)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(5)
    void shouldGetBooksByAvailability() throws Exception {
        int count = 1;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/books/availability")
                        .param("count", String.valueOf(count))
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }
    protected boolean isSuccess(MvcResult mvcResult) throws Exception {
        String responseContent = mvcResult.getResponse().getContentAsString();
        RestResponse<?> response = objectMapper.readValue(responseContent, RestResponse.class);
        return response.isSuccess();
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(6)
    void shouldUpdateBookInventoryCount() throws Exception {
        Long bookId = 1L;
        Integer count = 5;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/books/{id}/inventoryCount/{count}", bookId, count)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @Order(7)
    void shouldUpdateBook() throws Exception {
        Long bookId = 1L;
        String request = """
            {
                "title": "Yeni Başlık",
                "author": "Yeni Yazar",
                "isbn": "12345678901",
                "publishedDate": "2020-01-01",
                "publisher": "Yeni Yayınevi",
                "genre": "Roman",
                "description": "Güncellenmiş açıklama",
                "pageCount": 300,
                "inventoryCount": 10
            }
            """;

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/books/{id}", bookId)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        boolean success = isSuccess(mvcResult);
        assertTrue(success);
    }
}

