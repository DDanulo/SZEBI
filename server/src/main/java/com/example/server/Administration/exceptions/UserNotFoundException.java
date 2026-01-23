package com.example.server.Administration.exceptions;


import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UserNotFoundException extends AppBaseException {
    public UserNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND, "Client " + id + " not found");
    }
    public UserNotFoundException(String login) {
        super(HttpStatus.NOT_FOUND, "Client " + login + " not found");
    }
}