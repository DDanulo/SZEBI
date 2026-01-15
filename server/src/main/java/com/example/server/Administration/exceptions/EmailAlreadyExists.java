package com.example.server.Administration.exceptions;

import org.springframework.http.HttpStatus;


public class EmailAlreadyExists extends AppBaseException {
    public EmailAlreadyExists(String email) {
        super(HttpStatus.CONFLICT, "Email " + email + " already exists");
    }
}

