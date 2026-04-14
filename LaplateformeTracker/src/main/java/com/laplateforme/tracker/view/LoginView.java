package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.AuthController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginView {

    private final AuthController authController = new AuthController();

    public void show(Stage stage) {

        Label title = new Label("Connexion");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Nom d'utilisateur");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Se connecter");
        loginBtn.setOnAction(e -> {
            boolean ok = authController.login(
                    usernameField.getText(),
                    passwordField.getText()
            );

            if (ok) {
                new MainView().show(stage);
            } else {
                errorLabel.setText("Identifiants incorrects.");
            }
        });

        VBox root = new VBox(10, title, usernameField, passwordField, loginBtn, errorLabel);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 40;");

        stage.setScene(new Scene(root, 350, 300));
        stage.setTitle("Login");
        stage.show();
    }
}
