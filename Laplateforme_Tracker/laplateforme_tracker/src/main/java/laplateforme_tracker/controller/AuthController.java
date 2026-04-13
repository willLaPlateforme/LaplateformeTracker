package laplateforme_tracker.controller;

import service.AuthService;

/**
 * Contrôleur d'authentification.
 * Fait le lien entre LoginView et AuthService.
 */
public class AuthController {

    private final AuthService authService = new AuthService();

    /**
     * Tente de connecter l'utilisateur.
     * @return true si le login réussit.
     */
    public boolean login(String username, String password) {
        if (username == null || username.isBlank()) {
            System.err.println(" Ah tu es un sans nom dommage .");
            return false;
        }
        if (password == null || password.isEmpty()) {
            System.err.println(" Le mot de passe est vide débile.");
            return false;
        }
        return authService.authenticate(username, password);
    }
}