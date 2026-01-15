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
        validateDate(dto.getTurnOn(), "Data włączenia");
        validateDate(dto.getTurnOff(), "Data wyłączenia");
        checkOverlap(dto.getDeviceId(), dto.getTurnOn(), dto.getTurnOff());
        if(dto.getTurnOn().isEqual(dto.getTurnOff())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Konflikt! Nie można wyłączyć i włączyć o tej samej chwili.");
        }

        Schedule schedule = Schedule.builder()
                .deviceId(dto.getDeviceId())
                .dateTimeTurnOn(dto.getTurnOn())
                .dateTimeTurnOff(dto.getTurnOff())
                .isRecurring(dto.isRecurring())
                .build();

        Schedule saved = scheduleRepo.save(schedule);
        return mapToDTO(saved);
    }

    private void checkOverlap(UUID deviceId, LocalDateTime newStart, LocalDateTime newEnd) {
        List<Schedule> existing = scheduleRepo.findByDeviceId(deviceId);

        for (Schedule s : existing) {

            if (newStart.isBefore(s.getDateTimeTurnOff()) && newEnd.isAfter(s.getDateTimeTurnOn())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Konflikt! W tym czasie istnieje już harmonogram dla tego urządzenia.");
            }
        }
    }

    public ScheduleDTO updateDailySchedule(ScheduleDTO dto) {

        validateDate(dto.getTurnOn(), "Data włączenia");
        validateDate(dto.getTurnOff(), "Data wyłączenia");


        Schedule schedule = scheduleRepo.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Harmonogram nie znaleziony"));


        schedule.setDateTimeTurnOn(dto.getTurnOn());
        schedule.setDateTimeTurnOff(dto.getTurnOff());
        schedule.setRecurring(dto.isRecurring());

        return mapToDTO(scheduleRepo.save(schedule));
    }
    public void deleteDailySchedule(UUID id) {
        scheduleRepo.deleteById(id);
    }

    public List<ScheduleDTO> findScheduleByDevice(UUID deviceId) {
        return scheduleRepo.findByDeviceId(deviceId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private void validateDate(LocalDateTime date, String fieldName) {
        if (date == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " jest wymagana!");
        }
        if (date.isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, fieldName + " nie może być w przeszłości!");
        }
    }

    private ScheduleDTO mapToDTO(Schedule s) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(s.getId());
        dto.setDeviceId(s.getDeviceId());
        dto.setTurnOn(s.getDateTimeTurnOn());
        dto.setTurnOff(s.getDateTimeTurnOff());
        dto.setRecurring(s.isRecurring());
        return dto;
    }
}