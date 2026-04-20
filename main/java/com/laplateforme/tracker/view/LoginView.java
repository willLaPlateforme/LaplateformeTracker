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
        usernameField.setMaxWidth(280);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Mot de passe");
        passwordField.setMaxWidth(280);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Se connecter");
        loginBtn.setMaxWidth(280);

        Hyperlink registerLink = new Hyperlink("Pas encore de compte ? S'inscrire");
        registerLink.setOnAction(e -> new RegisterView().show(stage));

        Runnable doLogin = () -> {
            boolean ok = authController.login(
                    usernameField.getText(),
                    passwordField.getText()
            );
            if (ok) {
                new MainView().show(stage);
            } else {
                errorLabel.setText("Identifiants incorrects.");
            }
        };

        loginBtn.setOnAction(e -> doLogin.run());
        passwordField.setOnAction(e -> doLogin.run());

        VBox root = new VBox(12, title, usernameField, passwordField,
                loginBtn, errorLabel, registerLink);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 40;");

        stage.setScene(new Scene(root, 380, 360));
        stage.setTitle("Connexion - LaPlateformeTracker");
        stage.show();
    }
}
