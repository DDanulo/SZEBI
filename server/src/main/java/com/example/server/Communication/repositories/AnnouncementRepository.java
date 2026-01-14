package com.example.server.Communication.repositories;

import com.example.server.Communication.objects.announcements.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findByBuilding(String building);
}
