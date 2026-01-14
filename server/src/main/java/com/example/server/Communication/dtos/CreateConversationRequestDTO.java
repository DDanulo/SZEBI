package com.example.server.Communication.dtos;

import java.util.UUID;

public record CreateConversationRequestDTO(UUID user1Id, UUID user2Id) {
}
