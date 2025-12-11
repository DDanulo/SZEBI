package com.example.server.Administration.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("empolyee")
@Data
@SuperBuilder
@NoArgsConstructor
public class Engineer extends User {
}
