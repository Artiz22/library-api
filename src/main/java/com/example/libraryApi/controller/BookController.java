package com.example.libraryApi.controller;

import com.example.libraryApi.dto.bookDto.BookRequestDTO;
import com.example.libraryApi.dto.bookDto.BookResponseDTO;
import com.example.libraryApi.service.BookService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/books")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "Books", description = "Operations related to books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Create a new book", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book successfully created",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Book already exists",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PostMapping
    public BookResponseDTO create(@RequestBody @Valid BookRequestDTO dto) {
        return bookService.create(dto);
    }

    @Operation(summary = "Update a book", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "409", description = "Book already exists",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public BookResponseDTO update(@PathVariable UUID id, @RequestBody @Valid BookRequestDTO dto) {
        return bookService.update(id, dto);
    }

    @Operation(summary = "Delete a book", security = {@SecurityRequirement(name = "bearerAuth")})
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Book deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Find a book by title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found",
                    content = @Content(schema = @Schema(implementation = BookResponseDTO.class))
            ),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(schema = @Schema(implementation = GlobalExceptionHandler.ErrorResponse.class))
            )
    })
    @GetMapping("/{title}")
    public BookResponseDTO findByTitle(@PathVariable String title) {
        return bookService.findByTitle(title);
    }

    @Operation(summary = "Get all books")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books page retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponseDTO.class)))
            )
    })
    @GetMapping
    public Page<BookResponseDTO> findAll(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return bookService.findAll(PageRequest.of(page, size));
    }

    @Operation(summary = "Find books by genre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books page filtered by genre",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponseDTO.class)))
            )
    })
    @GetMapping("/genre/{genre}")
    public Page<BookResponseDTO> findByGenre(@PathVariable String genre,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return bookService.findByGenre(genre, PageRequest.of(page, size));
    }

    @Operation(summary = "Find books by author name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books page filtered by author",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponseDTO.class)))
            )
    })
    @GetMapping("/author/{nameAuthor}")
    public Page<BookResponseDTO> findByAuthor(@PathVariable String nameAuthor,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return bookService.findByAuthor(nameAuthor, PageRequest.of(page, size));
    }

    @Operation(summary = "Search books by title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books page search query",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookResponseDTO.class)))
            )
    })
    @GetMapping("/search")
    public Page<BookResponseDTO> searchBooks(@RequestParam String query,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size) {
        return bookService.searchByTitle(query, PageRequest.of(page, size));
    }

}
