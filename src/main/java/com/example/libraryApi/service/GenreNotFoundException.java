package com.example.libraryApi.service;


public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException(String name) {
        super("Genre " + name + " not found");
    }
}
