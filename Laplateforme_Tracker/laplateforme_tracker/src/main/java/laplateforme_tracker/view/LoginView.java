package laplateforme_tracker.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Contrôleur de l'écran de connexion.
 * Pour l'instant, n'importe quel identifiant / mot de passe fonctionne.
 * La vraie logique d'authentification sera ajoutée par l'équipe backend.
 */
public class LoginView {

    @FXML private TextField champNomUtilisateur;
    @FXML private PasswordField champMotDePasse;
    @FXML private Label labelErreur;

    /**
     * Appelé quand l'utilisateur clique sur "Se connecter".
     * Vérifie juste que les champs ne sont pas vides, puis ouvre l'écran principal.
     */
    @FXML
    public void seConnecter() {
        String nom = champNomUtilisateur.getText().trim();
        String mdp = champMotDePasse.getText().trim();

        // Validation visuelle simple : champs non vides
        if (nom.isEmpty() || mdp.isEmpty()) {
            labelErreur.setText("Veuillez remplir tous les champs.");
            labelErreur.setVisible(true);
            return;
        }

        // Tout identifiant est accepté pour l'instant (prototype visuel)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();

            // On passe le nom d'utilisateur au contrôleur principal pour l'afficher
            MainView mainController = loader.getController();
            mainController.setNomUtilisateur(nom);

            Stage stage = (Stage) champNomUtilisateur.getScene().getWindow();
            stage.setTitle("Gestion Étudiants — " + nom);
            stage.setScene(new Scene(root, 1100, 700));
            stage.setResizable(true);
            stage.centerOnScreen();

        } catch (Exception e) {
            labelErreur.setText("Erreur lors du chargement : " + e.getMessage());
            labelErreur.setVisible(true);
        }
    }
}