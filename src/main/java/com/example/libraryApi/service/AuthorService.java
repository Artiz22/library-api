package com.example.libraryApi.service;

import com.example.libraryApi.dto.authorDto.AuthorRequestDTO;
import com.example.libraryApi.dto.authorDto.AuthorResponseDTO;
import com.example.libraryApi.entity.Author;
import com.example.libraryApi.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorResponseDTO> getAuthors() {
        try {
            return authorRepository.findAll().stream()
                    .map(author -> new AuthorResponseDTO(
                            author.getId(),
                            author.getFirstName(),
                            author.getLastName(),
                            author.getBiography(),
                            author.getBirthDate()
                    ))
                    .toList();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to fetch authors.", ex);
        }
    }

    public AuthorResponseDTO getAuthorById(UUID id) {
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new AuthorNotFoundException(id));

            return new AuthorResponseDTO(
                    author.getId(),
                    author.getFirstName(),
                    author.getLastName(),
                    author.getBiography(),
                    author.getBirthDate()
            );
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to fetch author with ID " + id, ex);
        }
    }

    public AuthorResponseDTO createAuthor(AuthorRequestDTO authorDTO) {
        try {
            Optional<Author> existingAuthor = authorRepository
                    .findOneByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(
                            authorDTO.firstName(),
                            authorDTO.lastName(),
                            authorDTO.birthDate()
                    );

            if (existingAuthor.isPresent()) {
                throw new AuthorAlreadyExistsException(
                        authorDTO.firstName(),
                        authorDTO.lastName(),
                        authorDTO.birthDate()
                );
            }

            Author author = new Author(
                    authorDTO.firstName(),
                    authorDTO.lastName(),
                    authorDTO.biography(),
                    authorDTO.birthDate()
            );

            return saveAndReturnDTO(author);

        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to create author.", ex);
        }
    }


    public AuthorResponseDTO updateAuthor(UUID id, AuthorRequestDTO authorDTO) {
        try {
            Author existingAuthor = authorRepository.findById(id)
                    .orElseThrow(() -> new AuthorNotFoundException(id));

            existingAuthor.setFirstName(authorDTO.firstName());
            existingAuthor.setLastName(authorDTO.lastName());
            existingAuthor.setBiography(authorDTO.biography());
            existingAuthor.setBirthDate(authorDTO.birthDate());

            return saveAndReturnDTO(existingAuthor);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to update author.", ex);
        }
    }

    public void deleteAuthor(UUID id) {
        try {
            if (!authorRepository.existsById(id)) {
                throw new AuthorNotFoundException(id);
            }
            authorRepository.deleteById(id);
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to delete author with ID " + id, ex);
        }
    }


    private AuthorResponseDTO saveAndReturnDTO(Author author) {
        UUID authorID = authorRepository.save(author).getId();
        return getAuthorById(authorID);
    }


}
