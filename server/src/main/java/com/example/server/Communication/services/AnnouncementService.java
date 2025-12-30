package com.example.server.Communication.services;

import com.example.server.Administration.model.User;
import com.example.server.Administration.repo.UserRepo;
import com.example.server.Communication.dtos.CreateAnnouncementDTO;
import com.example.server.Communication.exceptions.AnnouncementNotFoundException;
import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.announcements.Announcement;
import com.example.server.Communication.objects.announcements.AnnouncementStatus;
import com.example.server.Communication.repositories.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final UserRepo userRepository;

    @Transactional(readOnly = true)
    public List<Announcement> getAllAnnouncements(String building) {
        if (building != null && !building.isEmpty()) {
            return announcementRepository.findByBuilding(building);
        }
        return announcementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Announcement getAnnouncementById(Integer id) {
        return announcementRepository.findById(id)
                .orElseThrow(() -> new AnnouncementNotFoundException(id));
    }

    @Transactional
    public Announcement createAnnouncement(CreateAnnouncementDTO request) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new UserNotFoundException("User with id " + request.authorId() + " not found."));

        Announcement announcement = Announcement.builder()
                .author(author)
                .content(request.content())
                .timestamp(LocalDateTime.now())
                .level(request.level())
                .building(request.building())
                .status(AnnouncementStatus.OPEN)
                .build();

        return announcementRepository.save(announcement);
    }

    @Transactional
    public Announcement closeAnnouncement(Integer id) {
        Announcement announcement = getAnnouncementById(id);
        announcement.setStatus(AnnouncementStatus.CLOSED);
        return announcementRepository.save(announcement);
    }
}
