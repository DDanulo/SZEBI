package com.example.server.Administration.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("resident")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Resident extends User {
    @Column(name="room")
    private String room;


}
