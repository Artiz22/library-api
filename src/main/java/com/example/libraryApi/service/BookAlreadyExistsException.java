package com.example.libraryApi.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String title, UUID authorId) {
        super("Book already exists with title: \"" + title + "\" and author id: " + authorId);
    }
}