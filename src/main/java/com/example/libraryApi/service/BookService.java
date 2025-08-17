package com.example.libraryApi.service;

import com.example.libraryApi.dto.bookDto.BookRequestDTO;
import com.example.libraryApi.dto.bookDto.BookResponseDTO;
import com.example.libraryApi.entity.Author;
import com.example.libraryApi.entity.Genre;
import com.example.libraryApi.repository.AuthorRepository;
import com.example.libraryApi.repository.BookRepository;
import com.example.libraryApi.repository.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import com.example.libraryApi.entity.Book;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    public BookResponseDTO create(BookRequestDTO dto) {
        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.authorId()));

        List<Genre> genres = genreRepository.findAllById(dto.genreIds());

        // Vérification de conflit : titre + auteur
        boolean exists = bookRepository.existsByTitleIgnoreCaseAndAuthorId(dto.title(), dto.authorId());
        if (exists) {
            throw new BookAlreadyExistsException(dto.title(), dto.authorId());
        }

        Book book = new Book(
                dto.title(),
                author,
                genres,
                dto.publicationDate(),
                dto.coverImageUrl(),
                dto.synopsis()
        );

        Book saved = bookRepository.save(book);
        return toDto(saved);
    }

    public BookResponseDTO update(UUID id, BookRequestDTO dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Author author = authorRepository.findById(dto.authorId())
                .orElseThrow(() -> new AuthorNotFoundException(dto.authorId()));

        List<Genre> genres = genreRepository.findAllById(dto.genreIds());

        // Vérification de conflit uniquement si titre ou auteur changent
        if (!book.getTitle().equalsIgnoreCase(dto.title()) || !book.getAuthor().getId().equals(dto.authorId())) {
            boolean exists = bookRepository.existsByTitleIgnoreCaseAndAuthorId(dto.title(), dto.authorId());
            if (exists) {
                throw new BookAlreadyExistsException(dto.title(), dto.authorId());
            }
        }

        book.setTitle(dto.title());
        book.setAuthor(author);
        book.setGenres(genres);
        book.setPublicationDate(dto.publicationDate());
        book.setCoverImageUrl(dto.coverImageUrl());
        book.setSynopsis(dto.synopsis());

        Book updated = bookRepository.save(book);
        return toDto(updated);
    }

    public BookResponseDTO findByTitle(String title) {
        Book book = bookRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new BookNotFoundException(title));
        return toDto(book);
    }


    public void deleteById(UUID id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }



    public Page<BookResponseDTO> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(this::toDto);
    }

    public Page<BookResponseDTO> findByGenre(String genreName, Pageable pageable) {
        return bookRepository.findByGenres_NameIgnoreCase(genreName, pageable)
                .map(this::toDto);
    }

    public Page<BookResponseDTO> findByAuthor(String authorName, Pageable pageable) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new IllegalArgumentException("authorName must not be empty");
        }

        List<Author> authors = authorRepository.searchByFullName(authorName);

        if (authors.isEmpty()) {
            throw new AuthorNotFoundException(authorName);
        }

        List<UUID> authorIds = authors.stream()
                .map(Author::getId)
                .toList();

        return bookRepository.findByAuthorIdIn(authorIds, pageable)
                .map(this::toDto);
    }

    public Page<BookResponseDTO> searchByTitle(String partialTitle, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(partialTitle, pageable)
                .map(this::toDto);
    }



    private BookResponseDTO toDto(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName(),
                book.getGenres().stream().map(Genre::getName).toList(),
                book.getPublicationDate(),
                book.getCoverImageUrl(),
                book.getSynopsis()
        );
    }
}
