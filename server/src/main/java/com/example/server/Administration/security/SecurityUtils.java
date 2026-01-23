package com.example.server.Administration.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


import java.util.UUID;

@Component
public class SecurityUtils {
    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UUID)) {
            throw new AccessDeniedException("Brak autoryzacji lub niepoprawny użytkownik");
        }
        return (UUID) auth.getPrincipal();
    }

    public static String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            return "ANONYMOUS";
        }
        return auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }

    public static boolean isAdmin() {
        return "ADMIN".equals(getCurrentUserRole());
    }

    public static boolean isEngineer() {
        return "ENGINEER".equals(getCurrentUserRole());
    }
}
