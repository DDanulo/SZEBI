package com.example.server.Communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConversationNotFoundException extends ResponseStatusException {
    public ConversationNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
