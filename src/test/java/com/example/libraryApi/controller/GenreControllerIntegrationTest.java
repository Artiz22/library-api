package com.example.libraryApi.controller;
import com.example.libraryApi.dto.genreDto.GenreRequestDTO;
import com.example.libraryApi.dto.genreDto.GenreResponseDTO;
import com.example.libraryApi.service.GenreNotFoundException;
import com.example.libraryApi.service.GenreService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class GenreControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GenreService genreService;

    @BeforeEach
    void clearDatabase(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();
    }


    @Test
    void shouldReturnAllGenres() throws Exception {
        // When
        String responseJson = mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Then
        List<Map<String, Object>> genres = objectMapper.readValue(
                responseJson,
                new TypeReference<List<Map<String, Object>>>() {
                }
        );
        Assertions.assertThat(genres)
                .extracting(g -> g.get("name"), g -> g.get("description"))
                .containsExactlyInAnyOrder(
                        Tuple.tuple("Science Fiction", "Genre focused on speculative scientific concepts, futuristic settings, space exploration, time travel, and advanced technology."),
                        Tuple.tuple("Historical Fiction", "Genre set in the past, often featuring real historical events and characters with fictional elements."),
                        Tuple.tuple("Adventure", "Genre involving exciting journeys, exploration, and challenges in exotic or dangerous settings.")
                );
    }

    @Test
    void shouldCreateGenre() throws Exception {
        // Given
        GenreRequestDTO request = new GenreRequestDTO("Horror", "Scary and suspenseful stories");

        // When
        String responseJson = mockMvc.perform(post("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GenreResponseDTO created = objectMapper.readValue(responseJson, GenreResponseDTO.class);

        // Then
        Assertions.assertThat(created.name()).isEqualTo("Horror");
        Assertions.assertThat(created.description()).isEqualTo("Scary and suspenseful stories");
        Assertions.assertThat(created.id()).isNotNull();
    }

    @Test
    void shouldUpdateGenre() throws Exception {
        // Given
        GenreRequestDTO request = new GenreRequestDTO("Sci-Fi", "Updated description");

        // When
        String responseJson = mockMvc.perform(put("/genres/Science Fiction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GenreResponseDTO updated = objectMapper.readValue(responseJson, GenreResponseDTO.class);

        // Then
        Assertions.assertThat(updated.name()).isEqualTo("Sci-Fi");
        Assertions.assertThat(updated.description()).isEqualTo("Updated description");
    }

    @Test
    void shouldDeleteGenre() throws Exception {
        mockMvc.perform(delete("/genres/Adventure"))
                .andExpect(status().isNoContent());

        String responseJson = mockMvc.perform(get("/genres"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertFalse(responseJson.contains("Adventure"));
    }



    @Test
    void shouldThrowGenreNotFoundExceptionWhenUpdatingNonExistingGenre() throws Exception {
        GenreRequestDTO request = new GenreRequestDTO("Magie", "Description magique");

        GenreNotFoundException exception = assertThrows(GenreNotFoundException.class, () -> {
            genreService.update("Magie", request);
        });

        Assertions.assertThat(exception.getMessage()).isEqualTo("Genre Magie not found");
    }
}
