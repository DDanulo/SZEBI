package com.example.server.Communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageNotFoundException extends ResponseStatusException {
    public MessageNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
