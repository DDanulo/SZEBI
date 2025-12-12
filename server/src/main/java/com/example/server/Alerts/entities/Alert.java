package com.example.server.Alerts.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private AlertLevel level;
    private String source;
    private String message;
    //private Object context;

    @Builder
    public Alert(UUID id, LocalDateTime timestamp, AlertLevel level, String source, String message/*,Object context*/) {
        this.id = id;
        this.timestamp = timestamp;
        this.level = level;
        this.source = source;
        this.message = message;
        //this.context = context;
    }




}

