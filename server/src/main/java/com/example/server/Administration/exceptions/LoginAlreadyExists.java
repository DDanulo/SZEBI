package com.example.server.Administration.exceptions;


import org.springframework.http.HttpStatus;

public class LoginAlreadyExists extends AppBaseException {
    public LoginAlreadyExists(String login) {
        super(HttpStatus.CONFLICT, "Login " + login + " already exists");
    }
}
