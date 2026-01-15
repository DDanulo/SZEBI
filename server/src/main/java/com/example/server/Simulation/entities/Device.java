package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.UUID;
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public abstract  class Device {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;
    @Column
    private String description;
    @Column
    private boolean working;

    @Column
    protected int maxPowerPerHour = 0;

    @Column(name = "owner_id")
    private UUID ownerId;

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
