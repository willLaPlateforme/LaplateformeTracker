package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.StudentController;
import com.laplateforme.tracker.model.Student;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StudentFormView {

    private final StudentController controller = new StudentController();

    public void show(Stage stage, Student student) {

        Label title = new Label(student == null ? "Ajouter un étudiant" : "Modifier un étudiant");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField firstField = new TextField(student == null ? "" : student.getFirstName());
        firstField.setPromptText("Prénom");

        TextField lastField = new TextField(student == null ? "" : student.getLastName());
        lastField.setPromptText("Nom");

        TextField ageField = new TextField(student == null ? "" : String.valueOf(student.getAge()));
        ageField.setPromptText("Âge");

        TextField gradeField = new TextField(student == null ? "" : String.valueOf(student.getGrade()));
        gradeField.setPromptText("Note");

        TextField emailField = new TextField(student == null ? "" :
                (student.getEmail() == null ? "" : student.getEmail()));
        emailField.setPromptText("Email (optionnel)");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        Button saveBtn = new Button("Enregistrer");
        saveBtn.setOnAction(e -> {

            String error;
            if (student == null) {
                error = controller.addStudent(
                        firstField.getText(),
                        lastField.getText(),
                        ageField.getText(),
                        gradeField.getText(),
                        emailField.getText()
                );
            } else {
                error = controller.updateStudent(
                        student.getId(),
                        firstField.getText(),
                        lastField.getText(),
                        ageField.getText(),
                        gradeField.getText(),
                        emailField.getText()
                );
            }

            if (error != null) {
                errorLabel.setText(error);
            } else {
                new StudentListView().show(stage);
            }
        });

        Button cancelBtn = new Button("Annuler");
        cancelBtn.setOnAction(e -> new StudentListView().show(stage));

        HBox btnBox = new HBox(10, saveBtn, cancelBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(12, title, firstField, lastField, ageField, gradeField, emailField, errorLabel, btnBox);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(root, 400, 450));
        stage.setTitle("Formulaire étudiant");
        stage.show();
    }
}
