package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public abstract  class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column
    private String description;
    @Column
    private boolean working;
    @Column
    protected double area;
    @Column
    protected int maxPowerPerHour = 0;

    @Column
    protected int minWindSpeedForMaxPower = 0;

    @Column
    protected BigDecimal totalConsumed = BigDecimal.ZERO;

    @Column
    protected BigDecimal totalGenerated = BigDecimal.ZERO;

    public Device(String description, boolean working) {
        this.working = working;
        this.description = description;
    }

    public void activate() {
        this.working = true;
    }
    public void deactivate() {
        this.working = false;
    }
}
