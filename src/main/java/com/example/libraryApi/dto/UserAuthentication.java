package com.example.libraryApi.dto;


public record UserAuthentication(
        String login,
        String token
) {
}