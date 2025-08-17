package com.example.libraryApi.dto.bookDto;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookResponseDTO(
        UUID id,
        String title,
        String authorId,
        List<String> genreId,
        LocalDate publicationDate,
        String coverImageUrl,
        String synopsis


) {
}
