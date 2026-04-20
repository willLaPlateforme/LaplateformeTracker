package com.laplateforme.tracker.controller;

import com.laplateforme.tracker.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de AuthController.
 * AuthController.login()    retourne boolean.
 * AuthController.register() retourne String (null = succes, message = erreur).
 */
@DisplayName("AuthController")
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private AuthService mockAuth;
    private AuthController ctrl;

    @BeforeEach
    void setUp() throws Exception {
        ctrl = new AuthController();
        var f = AuthController.class.getDeclaredField("authService");
        f.setAccessible(true);
        f.set(ctrl, mockAuth);
    }

    // ── login retourne boolean ────────────────────────────────────────────────

    @Test void loginValide() {
        when(mockAuth.authenticate("admin", "Pass1234")).thenReturn(true);
        assertTrue(ctrl.login("admin", "Pass1234"));
        verify(mockAuth).authenticate("admin", "Pass1234");
    }

    @Test void loginMauvaisPass() {
        when(mockAuth.authenticate("admin", "mauvais")).thenReturn(false);
        assertFalse(ctrl.login("admin", "mauvais"));
    }

    @Test void loginUsernameNull() {
        assertFalse(ctrl.login(null, "Pass1234"));
        verify(mockAuth, never()).authenticate(any(), any());
    }

    @Test void loginUsernameVide() {
        assertFalse(ctrl.login("", "Pass1234"));
        verify(mockAuth, never()).authenticate(any(), any());
    }

    @Test void loginUsernameEspaces() {
        assertFalse(ctrl.login("   ", "Pass1234"));
        verify(mockAuth, never()).authenticate(any(), any());
    }

    @Test void loginPasswordNull() {
        assertFalse(ctrl.login("user", null));
        verify(mockAuth, never()).authenticate(any(), any());
    }

    @Test void loginPasswordVide() {
        assertFalse(ctrl.login("user", ""));
        verify(mockAuth, never()).authenticate(any(), any());
    }

    // ── register retourne String (null = OK, message = erreur) ───────────────

    @Test void registerValide() {
        when(mockAuth.register("newuser", "Pass1234")).thenReturn(true);
        assertNull(ctrl.register("newuser", "Pass1234"),
                "register valide doit retourner null");
        verify(mockAuth).register("newuser", "Pass1234");
    }

    @Test void registerUsernameExistant() {
        when(mockAuth.register("admin", "Pass1234")).thenReturn(false);
        String result = ctrl.register("admin", "Pass1234");
        assertNotNull(result, "username existant doit retourner un message d'erreur");
    }

    @Test void registerUsernameNull() {
        String result = ctrl.register(null, "Pass1234");
        assertNotNull(result);
        verify(mockAuth, never()).register(any(), any());
    }

    @Test void registerUsernameVide() {
        String result = ctrl.register("", "Pass1234");
        assertNotNull(result);
        verify(mockAuth, never()).register(any(), any());
    }

    @Test void registerPassTropCourt() {
        // "abc" = 3 chars, < 8 requis => erreur avant d'appeler authService
        String result = ctrl.register("user", "abc");
        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("8") ||
                   result.toLowerCase().contains("caractere") ||
                   result.toLowerCase().contains("minimum"));
        verify(mockAuth, never()).register(any(), any());
    }

    @Test void registerPassSansMajuscule() {
        String result = ctrl.register("user", "password1");
        assertNotNull(result, "Sans majuscule doit retourner une erreur");
        verify(mockAuth, never()).register(any(), any());
    }

    @Test void registerPassSansChiffre() {
        String result = ctrl.register("user", "Password");
        assertNotNull(result, "Sans chiffre doit retourner une erreur");
        verify(mockAuth, never()).register(any(), any());
    }

    @Test void registerPassExactement8Chars() {
        when(mockAuth.register("user", "Pass123!")).thenReturn(true);
        assertNull(ctrl.register("user", "Pass123!"),
                "Mot de passe de 8 chars valide doit passer");
    }

    @Test void registerPassNull() {
        String result = ctrl.register("user", null);
        assertNotNull(result);
        verify(mockAuth, never()).register(any(), any());
    }
}