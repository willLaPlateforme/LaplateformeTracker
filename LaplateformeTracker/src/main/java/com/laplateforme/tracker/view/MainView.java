package com.laplateforme.tracker.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {

    public void show(Stage stage) {

        Label title = new Label("Menu principal");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button listBtn = new Button("Liste des etudiants");
        listBtn.setMaxWidth(220);
        listBtn.setOnAction(e -> new StudentListView().show(stage));

        Button statsBtn = new Button("Statistiques");
        statsBtn.setMaxWidth(220);
        statsBtn.setOnAction(e -> new StatsView().show(stage));

        Button logoutBtn = new Button("Deconnexion");
        logoutBtn.setMaxWidth(220);
        logoutBtn.setOnAction(e -> new LoginView().show(stage));

        VBox root = new VBox(15, title, listBtn, statsBtn, logoutBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 40;");

        stage.setScene(new Scene(root, 400, 320));
        stage.setTitle("Menu principal");
        stage.show();
    }
}