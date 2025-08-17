package com.example.libraryApi.controller;

import com.example.libraryApi.dto.bookDto.BookRequestDTO;
import com.example.libraryApi.dto.bookDto.BookResponseDTO;
import com.example.libraryApi.service.BookAlreadyExistsException;
import com.example.libraryApi.service.BookNotFoundException;
import com.example.libraryApi.service.BookService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookControllerIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        String responseJson = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Page<BookResponseDTO> books = objectMapper.readValue(
                responseJson,
                new TypeReference<RestResponsePage<BookResponseDTO>>() {} // Utilisation de la nouvelle classe
        );

        Assertions.assertThat(books.getContent())
                .extracting(BookResponseDTO::title)
                .containsExactlyInAnyOrder(
                        "Twenty Thousand Leagues Under the Seas",
                        "Les Misérables"
                );
    }

    @Test
    void shouldCreateBook() throws Exception {
        BookRequestDTO request = new BookRequestDTO(
                "Around the World in Eighty Days",
                "A classic adventure novel by Jules Verne.",
                UUID.fromString("aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa"), // Auteur existant
                List.of(UUID.fromString("11111111-1111-1111-1111-111111111111")), // Genre existant
                LocalDate.of(1873, 1, 30),
                "https://example.com/80days.jpg",
                "Phileas Fogg attempts to circumnavigate the globe in 80 days."
        );

        String responseJson = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponseDTO created = objectMapper.readValue(responseJson, BookResponseDTO.class);

        Assertions.assertThat(created.title()).isEqualTo("Around the World in Eighty Days");
        Assertions.assertThat(created.id()).isNotNull();
    }

    @Test
    void shouldUpdateBook() throws Exception {
        BookRequestDTO request = new BookRequestDTO(
                "Les Misérables - Updated",
                "Updated description",
                UUID.fromString("aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                List.of(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                LocalDate.of(1862, 4, 3),
                "https://example.com/lesmiserables_updated.jpg",
                "Updated synopsis"
        );

        String responseJson = mockMvc.perform(put("/books/bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookResponseDTO updated = objectMapper.readValue(responseJson, BookResponseDTO.class);

        Assertions.assertThat(updated.title()).isEqualTo("Les Misérables - Updated");
        Assertions.assertThat(updated.synopsis()).isEqualTo("Updated synopsis");
    }

    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/books/bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb"))
                .andExpect(status().isNoContent());

        String responseJson = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseJson.contains("Twenty Thousand Leagues Under the Seas"));
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenUpdatingNonExistingBook() {
        BookRequestDTO request = new BookRequestDTO(
                "Unknown Book",
                "Description",
                UUID.fromString("aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                List.of(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                LocalDate.now(),
                null,
                null
        );

        UUID uuid = UUID.randomUUID();
        BookNotFoundException exception = assertThrows(BookNotFoundException.class, () -> {
            bookService.update(uuid, request);
        });

        Assertions.assertThat(exception.getMessage())
                .isEqualTo("Book not found with id: "+uuid);
    }

    @Test
    void shouldThrowBookAlreadyExistsExceptionWhenCreatingDuplicateTitle() {
        BookRequestDTO request = new BookRequestDTO(
                "Les Misérables", // existe déjà
                "Duplicate book",
                UUID.fromString("aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaaa"),
                List.of(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                LocalDate.of(1862, 4, 3),
                null,
                null
        );

        BookAlreadyExistsException exception = assertThrows(BookAlreadyExistsException.class, () -> {
            bookService.create(request);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Book already exists with title: \"Les Misérables\" and author id: aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    }
}
