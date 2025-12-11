package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract  class Device {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column
    private boolean working;

    public Device(boolean working) {
        this.working = working;
    }

    public void activate() {
        this.working = true;
    }
    public void deactivate() {
        this.working = false;
    }
}
