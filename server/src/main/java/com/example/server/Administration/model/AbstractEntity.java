package com.example.server.Administration.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
public abstract class AbstractEntity {

    @Id
    @Column(name = "ID")
    private UUID id;

    @Version
    @Column(name="VERSION")
    private long version;
}
