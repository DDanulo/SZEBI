package com.example.server.Simulation.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Getter
@NoArgsConstructor
public abstract class EnergyMeasure {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column
    private LocalDateTime timestamp;

    @Column
    private double value;

    @JoinColumn(name = "device_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Device device;

    public EnergyMeasure(LocalDateTime timestamp, double value, Device device) {
        this.timestamp = timestamp;
        this.value = value;
        this.device = device;
    }
}
