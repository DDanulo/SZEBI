package com.example.server.Communication;

import com.example.server.Administration.model.Resident;
import com.example.server.Administration.model.User;
import com.example.server.Administration.repo.UserRepo;
import com.example.server.Communication.dtos.CreateAnnouncementDTO;
import com.example.server.Communication.exceptions.AnnouncementNotFoundException;
import com.example.server.Communication.exceptions.UserNotFoundException;
import com.example.server.Communication.objects.announcements.Announcement;
import com.example.server.Communication.objects.announcements.AnnouncementLevel;
import com.example.server.Communication.objects.announcements.AnnouncementStatus;
import com.example.server.Communication.repositories.AnnouncementRepository;
import com.example.server.Communication.services.AnnouncementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnnouncementServiceTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @Mock
    private UserRepo userRepository;

    @InjectMocks
    private AnnouncementService announcementService;

    private User testUser;
    private Announcement testAnnouncement;

    @BeforeEach
    void setUp() {
        testUser = new Resident();
        testUser.setId(UUID.randomUUID());
        testUser.setLogin("testuser");
        testUser.setPasswordHash("testpassword");

        testAnnouncement = Announcement.builder()
                .id(1)
                .author(testUser)
                .content("Test content")
                .timestamp(LocalDateTime.now())
                .level(AnnouncementLevel.INFO)
                .building("Building A")
                .status(AnnouncementStatus.OPEN)
                .build();
    }

    @Test
    void shouldReturnAllAnnouncementsWhenNoBuildingIsSpecified() {
        when(announcementRepository.findAll()).thenReturn(List.of(testAnnouncement));

        List<Announcement> announcements = announcementService.getAllAnnouncements(null);

        assertThat(announcements).hasSize(1);
        assertThat(announcements.get(0)).isEqualTo(testAnnouncement);
        verify(announcementRepository).findAll();
        verify(announcementRepository, never()).findByBuilding(anyString());
    }

    @Test
    void shouldReturnAnnouncementsForSpecificBuilding() {
        when(announcementRepository.findByBuilding("Building A")).thenReturn(List.of(testAnnouncement));

        List<Announcement> announcements = announcementService.getAllAnnouncements("Building A");

        assertThat(announcements).hasSize(1);
        assertThat(announcements.get(0).getBuilding()).isEqualTo("Building A");
        verify(announcementRepository).findByBuilding("Building A");
        verify(announcementRepository, never()).findAll();
    }

    @Test
    void shouldReturnAnnouncementByIdWhenFound() {
        when(announcementRepository.findById(1)).thenReturn(Optional.of(testAnnouncement));

        Announcement found = announcementService.getAnnouncementById(1);

        assertThat(found).isEqualTo(testAnnouncement);
    }

    @Test
    void shouldThrowExceptionWhenAnnouncementByIdNotFound() {
        when(announcementRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(AnnouncementNotFoundException.class, () -> announcementService.getAnnouncementById(1));
    }

    @Test
    void shouldCreateAnnouncementSuccessfully() {
        CreateAnnouncementDTO request = new CreateAnnouncementDTO("New content", testUser.getId(), AnnouncementLevel.CRITICAL, "Building B");
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(announcementRepository.save(any(Announcement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Announcement created = announcementService.createAnnouncement(request);

        assertThat(created.getContent()).isEqualTo("New content");
        assertThat(created.getAuthor()).isEqualTo(testUser);
        assertThat(created.getStatus()).isEqualTo(AnnouncementStatus.OPEN);
        verify(announcementRepository).save(any(Announcement.class));
    }

    @Test
    void shouldThrowExceptionWhenCreatingAnnouncementWithNonExistentUser() {
        UUID nonExistentUserId = UUID.randomUUID();
        CreateAnnouncementDTO request = new CreateAnnouncementDTO("Content", nonExistentUserId, AnnouncementLevel.INFO, "Building C");
        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> announcementService.createAnnouncement(request));
        verify(announcementRepository, never()).save(any(Announcement.class));
    }

    @Test
    void shouldCloseAnnouncementSuccessfully() {
        when(announcementRepository.findById(1)).thenReturn(Optional.of(testAnnouncement));
        when(announcementRepository.save(any(Announcement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Announcement closed = announcementService.closeAnnouncement(1);

        assertThat(closed.getStatus()).isEqualTo(AnnouncementStatus.CLOSED);
        verify(announcementRepository).save(testAnnouncement);
    }

    @Test
    void shouldKeepStatusClosedWhenClosingAlreadyClosedAnnouncement() {
        testAnnouncement.setStatus(AnnouncementStatus.CLOSED);
        when(announcementRepository.findById(1)).thenReturn(Optional.of(testAnnouncement));
        when(announcementRepository.save(any(Announcement.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Announcement closed = announcementService.closeAnnouncement(1);

        assertThat(closed.getStatus()).isEqualTo(AnnouncementStatus.CLOSED);
        verify(announcementRepository).save(testAnnouncement);
    }
}
