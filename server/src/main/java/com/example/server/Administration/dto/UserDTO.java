package com.example.server.Administration.dto;

import com.example.server.Administration.model.Role;

import java.util.Map;
import java.util.UUID;


public record UserDTO(
        UUID id,
        String login,
        String firstName,
        String lastName,
        String email,
        Boolean active,
        Role role,
        Map<String, Object> details
) {
   
}
