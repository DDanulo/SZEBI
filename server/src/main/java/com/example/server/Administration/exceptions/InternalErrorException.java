package com.example.server.Administration.exceptions;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends AppBaseException {
    public InternalErrorException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");
    }

}
