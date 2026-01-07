package com.example.server.Communication.dtos;

import com.example.server.Administration.model.User;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ParticipantDTO(
        UUID id,
        String login,
        String firstName,
        String lastName,
        String email,
        String room
) {
    public static ParticipantDTO fromEntity(User user) {
        return ParticipantDTO.builder()
                .id(user.getId())
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .room("B" + user.getId().toString().substring(0, 3))
                .build();
    }
}
