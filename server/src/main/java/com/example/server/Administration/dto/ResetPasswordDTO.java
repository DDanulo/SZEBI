package com.example.server.Administration.dto;

public record ResetPasswordDTO(
        String token,
        String password
) {
}
