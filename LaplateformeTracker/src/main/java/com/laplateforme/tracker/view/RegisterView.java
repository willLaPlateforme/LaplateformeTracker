package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Page d'inscription.
 * CORRECTIONS :
 * - utilise new LoginView().show(stage) au lieu de new LoginView(stage)
 * - auth.register() prend 2 arguments (username + password)
 * - validation du mot de passe de confirmation faite ici dans la View
 */
public class RegisterView {

    public void show(Stage stage) {

        AuthController auth = new AuthController();

        Label title = new Label("Creer un compte");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Nom d'utilisateur");
        userField.setMaxWidth(280);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Mot de passe (6 caracteres min)");
        passField.setMaxWidth(280);

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirmer le mot de passe");
        confirmField.setMaxWidth(280);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(280);

        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12;");

        Button registerBtn = new Button("Creer le compte");
        registerBtn.setMaxWidth(280);

        Hyperlink loginLink = new Hyperlink("Deja un compte ? Se connecter");
        loginLink.setOnAction(e -> new LoginView().show(stage));

        registerBtn.setOnAction(e -> {
            errorLabel.setText("");
            successLabel.setText("");

            String username = userField.getText().trim();
            String password = passField.getText();
            String confirm  = confirmField.getText();

            // Validations dans la View
            if (username.isBlank()) {
                errorLabel.setText("Nom d'utilisateur obligatoire."); return;
            }
            if (password.length() < 6) {
                errorLabel.setText("Mot de passe trop court (6 min)."); return;
            }
            if (!password.equals(confirm)) {
                errorLabel.setText("Les mots de passe ne correspondent pas."); return;
            }

            // AuthService.register prend 2 args : username + password
            boolean ok = auth.register(username, password);
            if (ok) {
                successLabel.setText("Compte cree ! Vous pouvez vous connecter.");
                userField.clear(); passField.clear(); confirmField.clear();
            } else {
                errorLabel.setText("Nom d'utilisateur deja utilise.");
            }
        });

        VBox root = new VBox(12,
                title, new Separator(),
                new Label("Nom d'utilisateur"), userField,
                new Label("Mot de passe"),      passField,
                new Label("Confirmer"),          confirmField,
                errorLabel, successLabel,
                registerBtn, loginLink);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(40));

        stage.setScene(new Scene(root, 400, 480));
        stage.setTitle("Inscription");
        stage.show();
    }
}
