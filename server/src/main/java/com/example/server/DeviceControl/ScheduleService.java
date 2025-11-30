package com.example.server.DeviceControl;

import com.example.server.DeviceControl.ScheduleDTO;
import com.example.server.DeviceControl.Schedule;
import com.example.server.DeviceControl.ScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepo scheduleRepo;

    public ScheduleDTO addDailySchedule(ScheduleDTO dto) {
        validateDates(dto.getStart(), dto.getEnd());

        Schedule schedule = Schedule.builder()
                .deviceId(dto.getDeviceId())
                .dateTimeTurnOn(dto.getStart())
                .dateTimeTurnOff(dto.getEnd())
                .build();

        Schedule saved = scheduleRepo.save(schedule);
        return mapToDTO(saved);
    }

    public void deleteDailySchedule(UUID id) {
        scheduleRepo.deleteById(id);
    }

    public ScheduleDTO updateDailySchedule(ScheduleDTO dto) {

        validateDates(dto.getStart(), dto.getEnd());

        Schedule schedule = scheduleRepo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        schedule.setDateTimeTurnOn(dto.getStart());
        schedule.setDateTimeTurnOff(dto.getEnd());

        return mapToDTO(scheduleRepo.save(schedule));
    }

    public List<ScheduleDTO> findScheduleByDevice(UUID deviceId) {
        return scheduleRepo.findByDeviceId(deviceId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


 private void validateDates(LocalDateTime start, LocalDateTime end) {
     LocalDateTime now = LocalDateTime.now();

     // Wymóg: Sprawdzenie czy data początkowa jest w czasie przeszłym
     if (start.isBefore(now)) {
         // Rzucamy 409 CONFLICT zgodnie ze slajdem 11
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Data startu nie może być w przeszłości!");
     }


     if (start.isAfter(end) || start.equals(end)) {
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Data startu musi być przed datą końca!");
     }
 }

    // Prosty mapper
    private ScheduleDTO mapToDTO(Schedule s) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(s.getId());
        dto.setDeviceId(s.getDeviceId());
        dto.setStart(s.getDateTimeTurnOn());
        dto.setEnd(s.getDateTimeTurnOff());
        return dto;
    }
}