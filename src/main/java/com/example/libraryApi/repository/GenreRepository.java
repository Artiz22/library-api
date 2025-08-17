package com.example.libraryApi.repository;

import com.example.libraryApi.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {

    Optional<Genre> findGenreByNameIgnoreCase(String genreName);

    boolean existsGenreByNameIgnoreCase(String genreName);
}
