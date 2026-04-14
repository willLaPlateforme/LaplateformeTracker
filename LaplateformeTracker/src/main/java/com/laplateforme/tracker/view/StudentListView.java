package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.StudentController;
import com.laplateforme.tracker.model.Student;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class StudentListView {

    private final StudentController controller = new StudentController();
    private TableView<Student> table;

    public void show(Stage stage) {

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, String> colFirst = new TableColumn<>("Prénom");
        colFirst.setCellValueFactory(c -> javafx.beans.property.SimpleStringProperty
                .simpleStringProperty(c.getValue().getFirstName()));

        TableColumn<Student, String> colLast = new TableColumn<>("Nom");
        colLast.setCellValueFactory(c -> javafx.beans.property.SimpleStringProperty
                .simpleStringProperty(c.getValue().getLastName()));

        TableColumn<Student, Number> colAge = new TableColumn<>("Âge");
        colAge.setCellValueFactory(c -> javafx.beans.property.SimpleIntegerProperty
                .integerProperty(c.getValue().getAge()));

        TableColumn<Student, Number> colGrade = new TableColumn<>("Note");
        colGrade.setCellValueFactory(c -> javafx.beans.property.SimpleDoubleProperty
                .doubleProperty(c.getValue().getGrade()));

        TableColumn<Student, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> javafx.beans.property.SimpleStringProperty
                .simpleStringProperty(c.getValue().getEmail() == null ? "—" : c.getValue().getEmail()));

        table.getColumns().addAll(colFirst, colLast, colAge, colGrade, colEmail);

        refreshTable();

        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher...");

        Button searchBtn = new Button("OK");
        searchBtn.setOnAction(e -> {
            List<Student> list = controller.searchStudents(searchField.getText());
            table.setItems(FXCollections.observableArrayList(list));
        });

        Button addBtn = new Button("Ajouter");
        addBtn.setOnAction(e -> new StudentFormView().show(stage, null));

        Button editBtn = new Button("Modifier");
        editBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) new StudentFormView().show(stage, s);
        });

        Button deleteBtn = new Button("Supprimer");
        deleteBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) {
                controller.deleteStudent(s.getId());
                refreshTable();
            }
        });

        Button backBtn = new Button("Retour");
        backBtn.setOnAction(e -> new MainView().show(stage));

        HBox searchBox = new HBox(10, searchField, searchBtn);
        searchBox.setAlignment(Pos.CENTER);

        HBox btnBox = new HBox(10, addBtn, editBtn, deleteBtn, backBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(15, searchBox, table, btnBox);
        root.setPadding(new Insets(20));

        stage.setScene(new Scene(root, 700, 500));
        stage.setTitle("Liste des étudiants");
        stage.show();
    }

    private void refreshTable() {
        List<Student> list = controller.getAllStudents();
        table.setItems(FXCollections.observableArrayList(list));
    }
}
