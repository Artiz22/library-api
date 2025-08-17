package com.example.libraryApi.service;

import com.example.libraryApi.dto.genreDto.GenreRequestDTO;
import com.example.libraryApi.dto.genreDto.GenreResponseDTO;
import com.example.libraryApi.entity.Genre;
import com.example.libraryApi.repository.GenreRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<GenreResponseDTO> findAll() {
        try {
            return genreRepository.findAll().stream()
                    .map(g -> new GenreResponseDTO(g.getId(), g.getName(), g.getDescription()))
                    .toList();
        } catch (DataAccessException ex) {
            throw new RuntimeException("An error occurred while retrieving genres", ex);
        }
    }

    public GenreResponseDTO create(GenreRequestDTO dto) {
        try {
            if (genreRepository.existsGenreByNameIgnoreCase(dto.name())) {
                throw new GenreAlreadyExistsException(dto.name());
            }

            Genre genre = new Genre(dto.name(), dto.description());
            Genre saved = genreRepository.save(genre);
            return new GenreResponseDTO(saved.getId(), saved.getName(), saved.getDescription());
        } catch (DataAccessException ex) {
            throw new RuntimeException("An error occurred while creating the genre", ex);
        }
    }

    public GenreResponseDTO update(String name, GenreRequestDTO dto) {
        try {
            Genre genre = genreRepository.findGenreByNameIgnoreCase(name)
                    .orElseThrow(() -> new GenreNotFoundException(name));

            genre.setName(dto.name());
            genre.setDescription(dto.description());

            Genre updated = genreRepository.save(genre);
            return new GenreResponseDTO(updated.getId(), updated.getName(), updated.getDescription());
        } catch (DataAccessException ex) {
            throw new RuntimeException("An error occurred while updating the genre", ex);
        }
    }

    public void deleteByName(String name) {
        try {
            Genre genre = genreRepository.findGenreByNameIgnoreCase(name)
                    .orElseThrow(() -> new GenreNotFoundException(name));
            genreRepository.delete(genre);
        } catch (DataAccessException ex) {
            throw new RuntimeException("An error occurred while deleting the genre", ex);
        }
    }
}
