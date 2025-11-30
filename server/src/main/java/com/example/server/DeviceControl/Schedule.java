package com.example.server.DeviceControl;

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

    @Column(name = "start_time", nullable = false)
    private LocalDateTime dateTimeTurnOn;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime dateTimeTurnOff;
}