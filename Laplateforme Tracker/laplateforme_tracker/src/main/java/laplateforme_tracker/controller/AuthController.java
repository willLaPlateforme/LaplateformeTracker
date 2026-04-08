package controller;

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
            System.err.println("❌ Le nom d'utilisateur est vide.");
            return false;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("❌ Le mot de passe est vide.");
            return false;
        }
        return authService.authenticate(username, password);
    }
}