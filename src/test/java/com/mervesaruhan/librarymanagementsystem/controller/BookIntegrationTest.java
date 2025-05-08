package com.mervesaruhan.librarymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mervesaruhan.librarymanagementsystem.general.TestConstants;
import com.mervesaruhan.librarymanagementsystem.model.dto.saveRequest.BookSaveRequestDto;
import com.mervesaruhan.librarymanagementsystem.restResponse.RestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "LIBRARIAN")
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
    void shouldSearchBooks() throws Exception {
        String keyword = "kitap";
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

