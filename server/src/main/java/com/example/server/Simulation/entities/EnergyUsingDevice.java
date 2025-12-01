package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
public class EnergyUsingDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    private String description;


    @Column
    private boolean active;


    public EnergyUsingDevice(String description, boolean active) {
        this.description = description;
        this.active = active;
    }
}
