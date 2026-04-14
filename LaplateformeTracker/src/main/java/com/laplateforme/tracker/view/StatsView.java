package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.StatsController;
import com.laplateforme.tracker.controller.StudentController;
import com.laplateforme.tracker.model.Student;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class StatsView {

    private final StudentController studentController = new StudentController();
    private final StatsController statsController = new StatsController();

    public void show(Stage stage) {

        List<Student> students = studentController.getAllStudents();

        double avg = statsController.getClassAverage(students);

        Map<String, Long> ageGroups = statsController.getCountByAgeGroup(students);
        Map<String, Long> mentions = statsController.getCountByMention(students);

        PieChart ageChart = new PieChart();
        ageChart.setTitle("Répartition par âge");
        ageGroups.forEach((k, v) -> ageChart.getData().add(new PieChart.Data(k, v)));

        PieChart mentionChart = new PieChart();
        mentionChart.setTitle("Répartition par mentions");
        mentions.forEach((k, v) -> mentionChart.getData().add(new PieChart.Data(k, v)));

        VBox root = new VBox(20, ageChart, mentionChart);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 600, 600));
        stage.setTitle("Statistiques");
        stage.show();
    }
}
