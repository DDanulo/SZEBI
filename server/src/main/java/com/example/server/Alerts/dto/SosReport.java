package com.example.server.Alerts.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class SosReport {

    private UUID userID;
    private String message;
    private String location;


}
