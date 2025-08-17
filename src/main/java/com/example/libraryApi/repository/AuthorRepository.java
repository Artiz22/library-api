package com.example.libraryApi.repository;

import com.example.libraryApi.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Optional<Author> findOneByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthDate(String firstName, String lastName, LocalDate birthDate);


    @Query("""
       SELECT author FROM Author author
       WHERE LOWER(CONCAT(author.firstName, ' ', author.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
          OR LOWER(CONCAT(author.lastName, ' ', author.firstName)) LIKE LOWER(CONCAT('%', :name, '%'))
       """)
    List<Author> searchByFullName(@Param("name") String name);



}


