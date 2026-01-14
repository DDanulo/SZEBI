package com.example.server.Administration.exceptions;

import org.springframework.http.HttpStatus;

import java.util.UUID;


public class UserInactiveException extends AppBaseException {
    public UserInactiveException(UUID id) {
        super(HttpStatus.CONFLICT, "User with id " + id + " is inactive");
    }
}
