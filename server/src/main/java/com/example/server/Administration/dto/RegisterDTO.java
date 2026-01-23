package com.example.server.Administration.dto;

import jakarta.validation.constraints.*;

public record RegisterDTO(

        @NotBlank
        String login,
        @NotBlank
        String password,
        String firstName,
        String lastName,
        @NotBlank @Email
        String email,
        @NotBlank
        String room
) {
}
