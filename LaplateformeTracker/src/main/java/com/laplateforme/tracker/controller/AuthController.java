package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.service.AuthService;

public class AuthController {

    private final AuthService authService = new AuthService();

    /** Retourne true si login OK */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        return authService.authenticate(username, password);
    }

    /** Retourne true si inscription OK, false si username deja pris */
    public boolean register(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.length() < 6) return false;
        return authService.register(username, password);
    }
}
