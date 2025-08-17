package com.example.libraryApi.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(
        name = "authors",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"first_name", "last_name", "birth_date"})
        }
)
public class Author {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(length = 2000)
    private String biography;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    public Author() {} // Obligatoire pour JPA

    public Author(String firstName, String lastName, String biography, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.biography = biography;
        this.birthDate = birthDate;
    }
// Getters / Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
