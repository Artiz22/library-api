package com.example.libraryApi.controller;


import com.example.libraryApi.dto.authorDto.AuthorRequestDTO;
import com.example.libraryApi.dto.authorDto.AuthorResponseDTO;
import com.example.libraryApi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "Authors", description = "Operations related to authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Get all authors")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authors list",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorResponseDTO.class)))
            )
    })
    @GetMapping
    public List<AuthorResponseDTO> getAuthors() {
        return authorService.getAuthors();
    }

    @Operation(summary = "Get author by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author found",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public AuthorResponseDTO getAuthor(@PathVariable UUID id) {
        return authorService.getAuthorById(id);
    }

    @Operation(summary = "Create a new author", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Author created successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Author already exists",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PostMapping
    public AuthorResponseDTO createAuthor(@RequestBody @Valid AuthorRequestDTO dto) {
        return authorService.createAuthor(dto);
    }

    @Operation(summary = "Update an author", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Author updated successfully",
                    content = @Content(schema = @Schema(implementation = AuthorResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public AuthorResponseDTO updateAuthor(@PathVariable UUID id, @RequestBody @Valid AuthorRequestDTO dto) {
        return authorService.updateAuthor(id, dto);
    }

    @Operation(summary = "Delete an author", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Author deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Author not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
    }

}
