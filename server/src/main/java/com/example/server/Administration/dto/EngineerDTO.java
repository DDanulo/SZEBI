package com.example.server.Administration.dto;

import java.util.UUID;

public record EngineerDTO(
        UUID id,
        String login,
        String firstName,
        String lastName,
        boolean active,
        String email
) {
}
