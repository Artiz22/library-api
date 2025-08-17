package com.example.libraryApi.service;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(UUID id) {
        super("Book not found with id: " + id);
    }
    public BookNotFoundException(String title) {
        super("Book not found with title: " + title);
    }
}