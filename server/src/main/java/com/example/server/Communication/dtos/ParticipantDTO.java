package com.example.server.Communication.dtos;

import com.example.server.Administration.model.User;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ParticipantDTO(
        UUID id,
        String login
) {
    public static ParticipantDTO fromEntity(User user) {
        return ParticipantDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();
    }
}
