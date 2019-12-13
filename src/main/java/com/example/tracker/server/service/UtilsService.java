package com.example.tracker.server.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class UtilsService {
    public static String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
