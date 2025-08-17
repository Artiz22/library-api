package com.example.libraryApi.dto.bookDto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record BookRequestDTO(
        @NotBlank
        @Size(min = 1, max = 100)
        String title,

        @NotBlank
        @Size(max = 2000)
        String description,

        @NotNull
        UUID authorId,

        @NotEmpty
        List<@NotNull UUID> genreIds,

        @NotNull
        @PastOrPresent
        LocalDate publicationDate,

        String coverImageUrl,


        String synopsis


) {
}
