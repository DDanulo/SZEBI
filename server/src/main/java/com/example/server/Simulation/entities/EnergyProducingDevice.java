package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@NoArgsConstructor
public  abstract class EnergyProducingDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    @Setter
    private Boolean working;

    @Column
    protected BigDecimal totalGenerated;



    public EnergyProducingDevice(boolean working) {
        this.working = working;
        this.totalGenerated = BigDecimal.valueOf(0.0f);
    }

    public abstract double generateEnergy(double coefficient);




}
