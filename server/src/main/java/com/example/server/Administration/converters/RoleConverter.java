package com.example.server.Administration.converters;

import com.example.server.Administration.model.*;


public class RoleConverter {
    public static Role roleFor(User user) {
        return switch (user) {
            case Administrator a -> Role.ADMIN;
            case Engineer e -> Role.ENGINEER;
            case Resident r -> Role.RESIDENT;
            default -> Role.RESIDENT;
        };
    }
}