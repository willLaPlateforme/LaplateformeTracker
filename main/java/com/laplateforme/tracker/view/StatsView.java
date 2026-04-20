package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.StatsController;
import com.laplateforme.tracker.controller.StudentController;
import com.laplateforme.tracker.model.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class StatsView {

    private final StudentController studentCtrl = new StudentController();
    private final StatsController   statsCtrl   = new StatsController();

    public void show(Stage stage) {

        List<Student> students = studentCtrl.getAllStudents();

        Map<String, Long> ageGroups = statsCtrl.getCountByAgeGroup(students);
        Map<String, Long> mentions  = statsCtrl.getCountByMention(students);

        PieChart ageChart = new PieChart();
        ageChart.setTitle("Repartition par age");
        ageGroups.forEach((k, v) -> ageChart.getData().add(new PieChart.Data(k, v)));

        PieChart mentionChart = new PieChart();
        mentionChart.setTitle("Repartition par mention");
        mentions.forEach((k, v) -> mentionChart.getData().add(new PieChart.Data(k, v)));

        Button backBtn = new Button("< Retour");
        backBtn.setOnAction(e -> new MainView().show(stage));

        VBox root = new VBox(20, ageChart, mentionChart, backBtn);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 650, 680));
        stage.setTitle("Statistiques");
        stage.show();
    }
}