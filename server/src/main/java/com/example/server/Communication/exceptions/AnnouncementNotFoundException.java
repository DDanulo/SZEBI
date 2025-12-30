package com.example.server.Communication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AnnouncementNotFoundException extends ResponseStatusException {
    public AnnouncementNotFoundException(Integer id) {
        super(HttpStatus.NOT_FOUND, "Announcement with id " + id + " not found.");
    }
}
