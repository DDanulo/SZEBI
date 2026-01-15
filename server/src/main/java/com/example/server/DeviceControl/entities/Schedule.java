package com.example.server.DeviceControl.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "schedule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "turn_on_time", nullable = false)
    private LocalDateTime dateTimeTurnOn;


    @Column(name = "turn_off_time", nullable = false)
    private LocalDateTime dateTimeTurnOff;

    @Column(name = "is_recurring")
    private boolean isRecurring;
}