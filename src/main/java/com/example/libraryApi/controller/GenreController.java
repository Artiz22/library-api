package com.example.libraryApi.controller;
import com.example.libraryApi.dto.genreDto.GenreRequestDTO;
import com.example.libraryApi.dto.genreDto.GenreResponseDTO;
import com.example.libraryApi.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/genres")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "Genres", description = "Operations related to genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @Operation(summary = "Get all genres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genres list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = GenreResponseDTO.class)))
            )
    })
    @GetMapping
    public List<GenreResponseDTO> getGenres() {
        return genreService.findAll();
    }

    @Operation(summary = "Create a new genre", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Genre created successfully",
                    content = @Content(schema = @Schema(implementation = GenreResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Genre already exists",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PostMapping
    public GenreResponseDTO createGenre(@RequestBody @Valid GenreRequestDTO dto) {
        return genreService.create(dto);
    }

    @Operation(summary = "Update a genre", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Genre updated successfully",
                    content = @Content(schema = @Schema(implementation = GenreResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PutMapping("/{name}")
    public GenreResponseDTO updateGenre(@PathVariable String name, @RequestBody @Valid GenreRequestDTO dto) {
        return genreService.update(name, dto);
    }

    @Operation(summary = "Delete a genre", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Genre deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Genre not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @DeleteMapping("/{name}")
    @ResponseStatus(NO_CONTENT)
    public void deleteGenre(@PathVariable String name) {
        genreService.deleteByName(name);
    }
}