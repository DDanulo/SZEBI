package com.example.server.DeviceControl;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ScheduleDTO {
    private UUID id;
    private UUID deviceId;
    private LocalDateTime turnOn;
    private LocalDateTime turnOff;
    private boolean recurring;
}