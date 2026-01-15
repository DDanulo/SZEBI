package com.example.server.Communication.controllers;

import com.example.server.Communication.dtos.AnnouncementDTO;
import com.example.server.Communication.dtos.CreateAnnouncementDTO;
import com.example.server.Communication.objects.announcements.Announcement;
import com.example.server.Communication.services.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/communication/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping
    public List<AnnouncementDTO> getAllAnnouncements(@RequestParam(required = false) String building) {
        return announcementService.getAllAnnouncements(building).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AnnouncementDTO getAnnouncementById(@PathVariable Integer id) {
        return convertToDto(announcementService.getAnnouncementById(id));
    }

    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody CreateAnnouncementDTO request) {
        Announcement announcement = announcementService.createAnnouncement(request);
        return new ResponseEntity<>(convertToDto(announcement), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/close")
    public AnnouncementDTO closeAnnouncement(@PathVariable Integer id) {
        return convertToDto(announcementService.closeAnnouncement(id));
    }

    private AnnouncementDTO convertToDto(Announcement announcement) {
        return new AnnouncementDTO(
                announcement.getId(),
                announcement.getContent(),
                announcement.getAuthor().getId(),
                announcement.getAuthor().getLogin(),
                announcement.getTimestamp(),
                announcement.getLevel(),
                announcement.getBuilding(),
                announcement.getStatus()
        );
    }
}
