package com.example.libraryApi.service;

import java.util.UUID;

public class AuthorNotFoundException extends RuntimeException {

    public AuthorNotFoundException(UUID id) {
        super("Author with ID " + id + " not found");
    }

    public AuthorNotFoundException(String fullName) {
        super("Author with name \"" + fullName + "\" not found");
    }
}
