package com.example.server.DeviceControl;

import com.example.server.DeviceControl.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleRepo extends JpaRepository<Schedule, UUID> {
    // Metoda z diagramu: findScheduleByUser
    // ale na razie zróbmy proste szukanie po urządzeniu bo nie mozemy po user)
    List<Schedule> findByDeviceId(UUID deviceId);
}