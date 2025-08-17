package com.example.libraryApi.service;

import java.time.LocalDate;

public class AuthorAlreadyExistsException extends RuntimeException {
    public AuthorAlreadyExistsException(String firstName, String lastName, LocalDate birthDate) {
        super("Author already exists with name: " + firstName + " " + lastName + " and birth date: " + birthDate);
    }
}
