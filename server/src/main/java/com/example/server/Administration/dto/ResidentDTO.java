package com.example.server.Administration.dto;

import jakarta.persistence.Column;
import lombok.Builder;

import java.util.UUID;

public record ResidentDTO (
        UUID id,
        String login,
        String firstName,
        String lastName,
        boolean active,
        String email,
        String room
) {}
