package com.example.server.Communication.dtos;

import com.example.server.Communication.objects.announcements.AnnouncementLevel;
import com.example.server.Communication.objects.announcements.AnnouncementStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record AnnouncementDTO(
        Integer id,
        String content,
        UUID authorId,
        String authorLogin,
        LocalDateTime timestamp,
        AnnouncementLevel level,
        String building,
        AnnouncementStatus status
) {
}
