package com.example.server.Administration.dto;

public record TokenDTO(
        String accessToken,
        String refreshToken
) {
}