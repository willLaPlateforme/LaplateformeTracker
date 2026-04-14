package com.laplateforme.tracker.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {

    public void show(Stage stage) {

        Button listBtn = new Button("Liste des étudiants");
        listBtn.setOnAction(e -> new StudentListView().show(stage));

        Button statsBtn = new Button("Statistiques");
        statsBtn.setOnAction(e -> new StatsView().show(stage));

        Button logoutBtn = new Button("Déconnexion");
        logoutBtn.setOnAction(e -> new LoginView().show(stage));

        VBox root = new VBox(15, listBtn, statsBtn, logoutBtn);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-padding: 40;");

        stage.setScene(new Scene(root, 400, 300));
        stage.setTitle("Menu principal");
        stage.show();
    }
}
