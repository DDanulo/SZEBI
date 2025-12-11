package com.example.server.Administration.dto;

import java.util.UUID;

public record UserDTO(
        String login,
        String firstName,
        String lastName,
        String email
) {
   
}
