package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterView {

    public void show(Stage stage) {

        AuthController auth = new AuthController();

        Label title = new Label("Creer un compte");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField userField = new TextField();
        userField.setPromptText("Nom d'utilisateur");
        userField.setMaxWidth(300);

        PasswordField passField = new PasswordField();
        passField.setPromptText("Mot de passe");
        passField.setMaxWidth(300);

        PasswordField confirmField = new PasswordField();
        confirmField.setPromptText("Confirmer le mot de passe");
        confirmField.setMaxWidth(300);

        // Indicateur des regles du mot de passe
        Label rulesLabel = new Label(
            "Regles : 8 caracteres min • 1 MAJUSCULE • 1 minuscule • 1 chiffre");
        rulesLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666; -fx-font-style: italic;");
        rulesLabel.setWrapText(true);
        rulesLabel.setMaxWidth(300);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12;");
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(300);

        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12;");

        Button registerBtn = new Button("Creer le compte");
        registerBtn.setMaxWidth(300);

        Hyperlink loginLink = new Hyperlink("Deja un compte ? Se connecter");
        loginLink.setOnAction(e -> new LoginView().show(stage));

        registerBtn.setOnAction(e -> {
            errorLabel.setText("");
            successLabel.setText("");

            String username = userField.getText().trim();
            String password = passField.getText();
            String confirm  = confirmField.getText();

            // Verification confirmation mot de passe (dans la View)
            if (!password.equals(confirm)) {
                errorLabel.setText("Les mots de passe ne correspondent pas.");
                return;
            }

            // Toute la validation (username + regles mdp + doublon) dans le controller
            String error = auth.register(username, password);
            if (error == null) {
                successLabel.setText("Compte cree ! Vous pouvez vous connecter.");
                userField.clear(); passField.clear(); confirmField.clear();
            } else {
                errorLabel.setText(error);
            }
        });

        VBox root = new VBox(10,
                title, new Separator(),
                new Label("Nom d'utilisateur"), userField,
                new Label("Mot de passe"),      passField,
                rulesLabel,
                new Label("Confirmer"),          confirmField,
                errorLabel, successLabel,
                registerBtn, loginLink);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(40));

        stage.setScene(new Scene(root, 420, 520));
        stage.setTitle("Inscription");
        stage.show();
    }
}
