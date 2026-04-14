package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.AuthController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Page d'inscription — manquait selon la remarque du prof.
 */
public class RegisterView {

    public RegisterView(Stage stage) {
        AuthController auth = new AuthController();

        Label title = new Label("Creer un compte");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));

        TextField userField = new TextField();
        userField.setPromptText("Nom d'utilisateur");
        userField.setMaxWidth(320);

        PasswordField passField = new PasswordField();
        passField.setMaxWidth(320);
        passField.setPromptText("Mot de passe (6 caracteres min)");

        PasswordField confirmField = new PasswordField();
        confirmField.setMaxWidth(320);
        confirmField.setPromptText("Confirmer le mot de passe");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 12;");
        errorLabel.setVisible(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(320);

        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-size: 12;");
        successLabel.setVisible(false);

        Button registerBtn = new Button("Creer le compte");
        registerBtn.setMaxWidth(320);
        registerBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 10 20; -fx-background-radius: 6;");

        Hyperlink loginLink = new Hyperlink("Deja un compte ? Se connecter");
        loginLink.setOnAction(e -> new LoginView(stage));

        registerBtn.setOnAction(e -> {
            errorLabel.setVisible(false);
            successLabel.setVisible(false);
            String err = auth.register(userField.getText(), passField.getText(), confirmField.getText());
            if (err == null) {
                successLabel.setText("Compte cree ! Vous pouvez vous connecter.");
                successLabel.setVisible(true);
                userField.clear(); passField.clear(); confirmField.clear();
            } else {
                errorLabel.setText(err);
                errorLabel.setVisible(true);
            }
        });

        VBox layout = new VBox(14,
            title, new Separator(),
            new Label("Nom d'utilisateur"), userField,
            new Label("Mot de passe"), passField,
            new Label("Confirmer"), confirmField,
            errorLabel, successLabel, registerBtn, loginLink);
        layout.setAlignment(Pos.CENTER_LEFT);
        layout.setPadding(new Insets(50));
        layout.setMaxWidth(400);

        StackPane root = new StackPane(layout);
        root.setStyle("-fx-background-color: #f1f5f9;");

        stage.setTitle("Inscription - LaPlateformeTracker");
        stage.setScene(new Scene(root, 520, 520));
        stage.setResizable(false);
        stage.show();
    }
}
