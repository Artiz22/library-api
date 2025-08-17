package com.example.libraryApi.dto.genreDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GenreRequestDTO( @NotBlank
                               @Size(max = 100) String name,
                               @Size(max = 1000) String description

) { }
