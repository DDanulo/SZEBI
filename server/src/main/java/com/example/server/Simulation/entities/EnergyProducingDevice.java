package com.example.server.Simulation.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@DiscriminatorValue("generating")
@NoArgsConstructor
public  abstract class EnergyProducingDevice extends Device {


    @Column
    protected BigDecimal totalGenerated;



    public EnergyProducingDevice(boolean working) {
        super(working);
        this.totalGenerated = BigDecimal.valueOf(0.0f);
    }

    public abstract double generateEnergy(double coefficient);




}
