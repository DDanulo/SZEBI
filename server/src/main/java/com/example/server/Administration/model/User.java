package com.example.server.Administration.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "Users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Access(AccessType.FIELD)
public abstract class User extends AbstractEntity {
    @NonNull
    @Column(name = "login", unique = true)
    private String login;
    @NonNull
    @Column(name="passwordhash")
    private String passwordHash;

    @Column(name="firstname")
    private String firstName;

    @Column(name="lastname")
    private String lastName;

    @Column(name="active")
    @Builder.Default
    private boolean active = false;

    @NonNull
    @Column(name="email", unique = true)
    private String email;
}
