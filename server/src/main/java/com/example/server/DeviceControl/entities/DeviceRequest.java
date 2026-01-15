package com.example.server.DeviceControl.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequest {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private UUID userId; // ID Użytkownika zgłaszającego

    @Enumerated(EnumType.STRING)
    private RequestType requestType; // ADD, REMOVE

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt;

    // Dane urządzenia (do stworzenia)
    private String deviceName;
    private String deviceType;
    private Double area;
    private Integer maxPower;
    private Integer minWind;

    // ID urządzenia (do usunięcia)
    private UUID targetDeviceId;

    public enum RequestType { ADD, REMOVE }
    public enum RequestStatus { PENDING, APPROVED, REJECTED }
}