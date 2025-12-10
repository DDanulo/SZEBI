package com.example.server.Communication.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record MessageDTO(
        UUID id,
        String content,
        String authorLogin,
        LocalDateTime timestamp
) {
}
