package com.example.libraryApi.controller;

import com.example.libraryApi.dto.authorDto.AuthorRequestDTO;
import com.example.libraryApi.dto.authorDto.AuthorResponseDTO;
import com.example.libraryApi.service.AuthorNotFoundException;
import com.example.libraryApi.service.AuthorService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void shouldReturnAllAuthors() throws Exception {
        // When
        String responseJson = mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        List<Map<String, Object>> authors = objectMapper.readValue(
                responseJson,
                new TypeReference<List<Map<String, Object>>>() {}
        );

        Assertions.assertThat(authors)
                .extracting(a -> a.get("firstName"), a -> a.get("lastName"))
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Jules", "Verne"),
                        Tuple.tuple("Victor", "Hugo"),
                        Tuple.tuple("Alexandre", "Dumas")
                );
    }

    @Test
    void shouldCreateAuthor() throws Exception {
        AuthorRequestDTO request = new AuthorRequestDTO(
                "George",
                "Orwell",
                "Author of 1984 and Animal Farm",
                LocalDate.of(1903, 6, 25)
        );

        String responseJson = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthorResponseDTO created = objectMapper.readValue(responseJson, AuthorResponseDTO.class);

        Assertions.assertThat(created.firstName()).isEqualTo("George");
        Assertions.assertThat(created.lastName()).isEqualTo("Orwell");
        Assertions.assertThat(created.id()).isNotNull();
    }

    @Test
    void shouldUpdateAuthor() throws Exception {
        AuthorRequestDTO request = new AuthorRequestDTO(
                "Louis",
                "Dumas",
                "Updated biography",
                LocalDate.of(1920, 1, 2)
        );

        String responseJson = mockMvc.perform(put("/authors/aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AuthorResponseDTO updated = objectMapper.readValue(responseJson, AuthorResponseDTO.class);

        Assertions.assertThat(updated.firstName()).isEqualTo("Louis");
        Assertions.assertThat(updated.biography()).isEqualTo("Updated biography");
    }

    @Test
    void shouldDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/authors/aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
                .andExpect(status().isNoContent());

        String responseJson = mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseJson.contains("Alexandre Dumas"));
    }

    @Test
    void shouldThrowAuthorNotFoundExceptionWhenUpdatingNonExistingAuthor() {
        AuthorRequestDTO request = new AuthorRequestDTO(
                "Eric",
                "Zola",
                "No biography",
                LocalDate.of(2000, 1, 1)
        );

        UUID uuid =UUID.randomUUID();
        AuthorNotFoundException exception = assertThrows(AuthorNotFoundException.class, () -> {
            authorService.updateAuthor(uuid, request);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Author with ID "+uuid+ " not found");
    }
}
