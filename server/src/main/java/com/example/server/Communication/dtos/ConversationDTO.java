package com.example.server.Communication.dtos;

import com.example.server.Communication.objects.Conversation;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ConversationDTO(
        Long id,
        List<ParticipantDTO> participants,
        List<MessageDTO> messages
) {
    public static ConversationDTO fromEntity(Conversation conversation) {
        return ConversationDTO.builder()
                .id(conversation.getId())
                .participants(conversation.getParticipants().stream()
                        .map(ParticipantDTO::fromEntity)
                        .collect(Collectors.toList()))
                .messages(conversation.getMessages().stream()
                        .map(MessageDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
