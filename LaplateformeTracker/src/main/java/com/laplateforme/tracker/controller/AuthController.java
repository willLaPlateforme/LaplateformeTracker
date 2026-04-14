package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.service.AuthService;

public class AuthController {

    private final AuthService authService = new AuthService();

    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;

        return authService.authenticate(username, password);
    }
}
