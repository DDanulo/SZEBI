package com.example.server.Administration.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("empolyee")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User{
}
