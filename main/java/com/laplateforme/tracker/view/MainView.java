package com.laplateforme.tracker.view;

import com.laplateforme.tracker.service.BackupService;
import com.laplateforme.tracker.util.DatabaseConnection;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainView {

    private final BackupService backupService = new BackupService();

    public void show(Stage stage) {

        Label title = new Label("Menu principal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ── Boutons principaux ──────────────────────────────────────────────
        Button listBtn = new Button("Liste des etudiants");
        listBtn.setMaxWidth(240);
        listBtn.setOnAction(e -> new StudentListView().show(stage));

        Button statsBtn = new Button("Statistiques");
        statsBtn.setMaxWidth(240);
        statsBtn.setOnAction(e -> new StatsView().show(stage));

        // ── Sauvegarde manuelle ─────────────────────────────────────────────
        Button backupBtn = new Button("Sauvegarder les donnees");
        backupBtn.setMaxWidth(240);
        backupBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        backupBtn.setOnAction(e -> {
            String path = backupService.backupNow();
            if (path != null) {
                showInfo("Sauvegarde reussie !\nFichier : " + path);
            } else {
                showError("Echec de la sauvegarde. Verifiez la connexion BDD.");
            }
        });

        // ── Deconnexion (retour login, BDD reste ouverte) ───────────────────
        Button logoutBtn = new Button("Deconnexion");
        logoutBtn.setMaxWidth(240);
        logoutBtn.setOnAction(e -> new LoginView().show(stage));

        // ── Quitter et fermer BDD ───────────────────────────────────────────
        Button quitBtn = new Button("Quitter (fermer BDD)");
        quitBtn.setMaxWidth(240);
        quitBtn.setStyle("-fx-background-color: #dc2626; -fx-text-fill: white;");
        quitBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Voulez-vous sauvegarder avant de quitter ?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            confirm.setTitle("Quitter l'application");
            confirm.setHeaderText("Fermeture de LaPlateformeTracker");
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.CANCEL) return;
                if (btn == ButtonType.YES) {
                    String path = backupService.backupNow();
                    if (path != null) showInfo("Sauvegarde OK : " + path);
                }
                // Fermer la connexion BDD proprement
                DatabaseConnection.closeConnection();
                Platform.exit();
            });
        });

        // ── Indicateur connexion BDD ────────────────────────────────────────
        Label dbStatus = new Label(DatabaseConnection.isConnected()
                ? "BDD connectee" : "BDD deconnectee");
        dbStatus.setStyle(DatabaseConnection.isConnected()
                ? "-fx-text-fill: green; -fx-font-size: 11px;"
                : "-fx-text-fill: red; -fx-font-size: 11px;");

        Separator sep = new Separator();

        VBox root = new VBox(12, title,
                listBtn, statsBtn,
                sep,
                backupBtn,
                logoutBtn, quitBtn,
                dbStatus);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));

        stage.setScene(new Scene(root, 400, 400));
        stage.setTitle("Menu principal");
        // Gerer la fermeture de la fenetre (croix rouge)
        stage.setOnCloseRequest(e -> {
            e.consume();
            quitBtn.fire();
        });
        stage.show();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
