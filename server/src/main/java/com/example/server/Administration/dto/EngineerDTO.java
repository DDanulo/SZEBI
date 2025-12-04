package com.example.server.Administration.dto;

import java.util.UUID;

public record EmployeeDTO(
        UUID id,
        String login,
        String firstName,
        String lastName,
        boolean active,
        String email
) {
}
