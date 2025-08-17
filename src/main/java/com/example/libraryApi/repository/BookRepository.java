package com.example.libraryApi.repository;

import com.example.libraryApi.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    Optional<Book> findByTitleIgnoreCase(String title);

    Page<Book> findByGenres_NameIgnoreCase(String genreName, Pageable pageable);


    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<Book> findByAuthorIdIn(List<UUID> authorIds, Pageable pageable);

    boolean existsByTitleIgnoreCaseAndAuthorId(String title, UUID authorId);


}
