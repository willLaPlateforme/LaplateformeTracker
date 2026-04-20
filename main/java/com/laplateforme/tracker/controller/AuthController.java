package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.service.AuthService;
import com.laplateforme.tracker.util.Validator;

public class AuthController {

    private final AuthService authService = new AuthService();

    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) return false;
        if (password == null || password.isBlank()) return false;
        return authService.authenticate(username, password);
    }

    /**
     * Retourne null si inscription OK.
     * Retourne un message d'erreur si le mot de passe est invalide ou le username pris.
     */
    public String register(String username, String password) {
        if (username == null || username.isBlank())
            return "Nom d'utilisateur obligatoire.";

        // Validation mot de passe renforcee
        String pwdError = Validator.validatePassword(password);
        if (pwdError != null) return pwdError;

        boolean ok = authService.register(username, password);
        if (!ok) return "Nom d'utilisateur deja utilise. Choisissez-en un autre.";
        return null;
    }
}
