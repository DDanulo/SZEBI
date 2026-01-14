package com.example.server.Communication.dtos;

import com.example.server.Communication.objects.Message;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MessageDTO(
        Long id,
        String content,
        UUID senderId,
        String senderLogin,
        LocalDateTime timestamp
) {
    public static MessageDTO fromEntity(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .senderLogin(message.getSender().getLogin())
                .timestamp(message.getTimestamp())
                .build();
    }
}
