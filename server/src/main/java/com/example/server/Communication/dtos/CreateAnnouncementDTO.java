package com.example.server.Communication.dtos;

import com.example.server.Communication.objects.announcements.AnnouncementLevel;

import java.util.UUID;

public record CreateAnnouncementDTO(
        String content,
        UUID authorId,
        AnnouncementLevel level,
        String building
) {
}
