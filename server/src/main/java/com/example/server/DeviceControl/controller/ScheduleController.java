package com.example.server.DeviceControl.controller;

import com.example.server.DeviceControl.dto.ScheduleDTO;
import com.example.server.DeviceControl.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleDTO> addSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.addDailySchedule(scheduleDTO));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<ScheduleDTO>> getSchedules(@PathVariable UUID deviceId) {
        return ResponseEntity.ok(scheduleService.findScheduleByDevice(deviceId));
    }

    @PutMapping
    public ResponseEntity<ScheduleDTO> updateSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return ResponseEntity.ok(scheduleService.updateDailySchedule(scheduleDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable UUID id) {
        scheduleService.deleteDailySchedule(id);
        return ResponseEntity.noContent().build();
    }
}