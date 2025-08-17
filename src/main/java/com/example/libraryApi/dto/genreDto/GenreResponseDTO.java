package com.example.libraryApi.dto.genreDto;

import java.util.UUID;


public record GenreResponseDTO(
        UUID id,
        String name,
        String description
) {}

