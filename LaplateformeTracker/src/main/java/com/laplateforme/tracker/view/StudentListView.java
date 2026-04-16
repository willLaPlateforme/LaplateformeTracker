package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.ImportExportController;
import com.laplateforme.tracker.controller.StudentController;
import com.laplateforme.tracker.model.Student;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Liste des etudiants avec :
 * - CRUD complet
 * - Recherche
 * - Tri par colonne
 * - Pagination (dans la View — remarque prof)
 * - Import / Export CSV et JSON
 */
public class StudentListView {

    private final StudentController      ctrl   = new StudentController();
    private final ImportExportController ioCtrl = new ImportExportController();

    // Pagination — geree dans la View
    private int currentPage = 1;
    private int pageSize    = 10;
    private String searchKeyword = "";

    private TableView<Student> table;
    private Label pageLabel;

    public void show(Stage stage) {

        // ── Tableau ──────────────────────────────────────────────────────────
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // CORRECTION : new SimpleStringProperty(valeur) et non SimpleStringProperty.simpleStringProperty()
        TableColumn<Student, String>  colFirst = new TableColumn<>("Prenom");
        colFirst.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));

        TableColumn<Student, String>  colLast  = new TableColumn<>("Nom");
        colLast.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));

        // CORRECTION : new SimpleIntegerProperty(v).asObject() pour TableColumn<Student, Integer>
        TableColumn<Student, Integer> colAge   = new TableColumn<>("Age");
        colAge.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAge()).asObject());

        // CORRECTION : new SimpleDoubleProperty(v).asObject()
        TableColumn<Student, Double>  colGrade = new TableColumn<>("Note");
        colGrade.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getGrade()).asObject());

        TableColumn<Student, String>  colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEmail() == null ? "-" : c.getValue().getEmail()));

        table.getColumns().addAll(colFirst, colLast, colAge, colGrade, colEmail);
        VBox.setVgrow(table, Priority.ALWAYS);

        // ── Recherche ────────────────────────────────────────────────────────
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par nom ou prenom...");
        searchField.setPrefWidth(220);
        searchField.textProperty().addListener((obs, old, val) -> {
            searchKeyword = val.trim();
            currentPage   = 1;
            refresh();
        });

        Button clearBtn = new Button("X");
        clearBtn.setOnAction(e -> { searchField.clear(); searchKeyword = ""; currentPage = 1; refresh(); });

        // ── Tri ──────────────────────────────────────────────────────────────
        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Nom", "Prenom", "Age", "Note");
        sortBox.setPromptText("Trier par...");

        Button sortBtn = new Button("Tri ASC");
        final boolean[] asc = {true};
        sortBtn.setOnAction(e -> {
            if (sortBox.getValue() == null) return;
            String col = switch (sortBox.getValue()) {
                case "Prenom" -> "first_name";
                case "Age"    -> "age";
                case "Note"   -> "grade";
                default       -> "last_name";
            };
            asc[0] = !asc[0];
            sortBtn.setText(asc[0] ? "Tri ASC" : "Tri DESC");
            List<Student> sorted = ctrl.getSortedStudents(col, asc[0] ? "ASC" : "DESC");
            table.setItems(FXCollections.observableArrayList(sorted));
            pageLabel.setText(sorted.size() + " etudiant(s)");
        });

        // ── Boutons CRUD ─────────────────────────────────────────────────────
        Button addBtn    = new Button("+ Ajouter");
        Button editBtn   = new Button("Modifier");
        Button deleteBtn = new Button("Supprimer");
        Button backBtn   = new Button("< Retour");

        addBtn.setOnAction(e -> new StudentFormView().show(stage, null));

        editBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s != null) new StudentFormView().show(stage, s);
            else alert(stage, "Selectionnez un etudiant.");
        });

        deleteBtn.setOnAction(e -> {
            Student s = table.getSelectionModel().getSelectedItem();
            if (s == null) { alert(stage, "Selectionnez un etudiant."); return; }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Supprimer " + s.getFirstName() + " " + s.getLastName() + " ?",
                    ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(b -> {
                if (b == ButtonType.YES) { ctrl.deleteStudent(s.getId()); refresh(); }
            });
        });

        backBtn.setOnAction(e -> new MainView().show(stage));

        // ── Import / Export ──────────────────────────────────────────────────
        Button exportCsvBtn  = new Button("Export CSV");
        Button exportJsonBtn = new Button("Export JSON");
        Button importCsvBtn  = new Button("Import CSV");

        exportCsvBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File f = fc.showSaveDialog(stage);
            if (f != null) {
                String err = ioCtrl.exportCSV(ctrl.getAllStudents(), f.getAbsolutePath());
                if (err != null) alert(stage, err);
                else info(stage, "Export reussi : " + f.getName());
            }
        });

        exportJsonBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File f = fc.showSaveDialog(stage);
            if (f != null) {
                String err = ioCtrl.exportJSON(ctrl.getAllStudents(), f.getAbsolutePath());
                if (err != null) alert(stage, err);
                else info(stage, "Export reussi : " + f.getName());
            }
        });

        importCsvBtn.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
            File f = fc.showOpenDialog(stage);
            if (f != null) {
                List<Student> imported = ioCtrl.importCSV(f.getAbsolutePath());
                for (Student s : imported)
                    ctrl.addStudent(s.getFirstName(), s.getLastName(),
                            String.valueOf(s.getAge()), String.valueOf(s.getGrade()),
                            s.getEmail());
                info(stage, imported.size() + " etudiant(s) importes.");
                refresh();
            }
        });

        // ── Pagination (dans la View — remarque prof) ────────────────────────
        Button prevBtn = new Button("< Precedent");
        Button nextBtn = new Button("Suivant >");
        pageLabel      = new Label();

        ComboBox<Integer> pageSizeBox = new ComboBox<>();
        pageSizeBox.getItems().addAll(5, 10, 20, 50);
        pageSizeBox.setValue(pageSize);
        pageSizeBox.setOnAction(e -> { pageSize = pageSizeBox.getValue(); currentPage = 1; refresh(); });

        prevBtn.setOnAction(e -> { if (currentPage > 1) { currentPage--; refresh(); } });
        nextBtn.setOnAction(e -> {
            int total   = ctrl.getTotalCount() ;
            int maxPage = Math.max(1, (int) Math.ceil((double) total / pageSize));
            if (currentPage < maxPage) { currentPage++; refresh(); }
        });

        // ── Assemblage ───────────────────────────────────────────────────────
        HBox searchBar = new HBox(8, searchField, clearBtn, sortBox, sortBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        HBox crudBar = new HBox(8, addBtn, editBtn, deleteBtn, backBtn,
                new Separator(), exportCsvBtn, exportJsonBtn, importCsvBtn);
        crudBar.setAlignment(Pos.CENTER_LEFT);

        HBox pagBar = new HBox(10, prevBtn, pageLabel, nextBtn,
                new Label("  Par page :"), pageSizeBox);
        pagBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(10, searchBar, crudBar, table, pagBar);
        root.setPadding(new Insets(16));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(root, 780, 560));
        stage.setTitle("Liste des etudiants");
        stage.show();

        refresh();
    }

    /** Rafraichit le tableau avec pagination ou recherche */
    private void refresh() {
        if (!searchKeyword.isBlank()) {
            List<Student> all   = ctrl.searchStudents(searchKeyword);
            int total           = all.size();
            int maxPage         = Math.max(1, (int) Math.ceil((double) total / pageSize));
            if (currentPage > maxPage) currentPage = maxPage;
            int from = (currentPage - 1) * pageSize;
            int to   = Math.min(from + pageSize, all.size());
            List<Student> page  = from < all.size() ? all.subList(from, to) : List.of();
            table.setItems(FXCollections.observableArrayList(page));
            pageLabel.setText("Page " + currentPage + "/" + maxPage + " (" + total + " resultats)");
        } else {
            int total   = ctrl.getTotalCount();
            int maxPage = Math.max(1, (int) Math.ceil((double) total / pageSize));
            if (currentPage > maxPage) currentPage = maxPage;
            List<Student> page  = ctrl.getPage(currentPage, pageSize);
            table.setItems(FXCollections.observableArrayList(page));
            pageLabel.setText("Page " + currentPage + "/" + maxPage + " (" + total + " etudiants)");
        }
    }

    private void alert(Stage stage, String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
    private void info(Stage stage, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
