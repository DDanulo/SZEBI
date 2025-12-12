package com.example.server.Administration.dto;

public record RegisterDTO(
        String login,
        String password,
        String firstName,
        String lastName,
        String email,
        String room
) {
}
