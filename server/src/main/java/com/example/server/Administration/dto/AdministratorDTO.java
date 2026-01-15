package com.example.server.Administration.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AdministratorDTO(
        UUID id,
        String login,
        String password,
        String firstName,
        String lastName,
        boolean active,
        String email
) {
}
