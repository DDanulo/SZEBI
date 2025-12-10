package com.example.server.Communication.objects;

import com.example.server.Administration.model.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "messages")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Message extends AbstractEntity {

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "author_login", nullable = false)
    private String authorLogin;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
