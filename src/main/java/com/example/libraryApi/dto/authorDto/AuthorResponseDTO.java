package com.example.libraryApi.dto.authorDto;

import java.time.LocalDate;
import java.util.UUID;

public record AuthorResponseDTO(
        UUID id,
        String firstName,
        String lastName,
        String biography,
        LocalDate birthDate
) {
}
