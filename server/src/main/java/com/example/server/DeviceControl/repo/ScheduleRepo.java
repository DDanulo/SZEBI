package com.example.server.DeviceControl.repo;

import com.example.server.DeviceControl.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, UUID> {
     List<Schedule> findByDeviceId(UUID deviceId);
}