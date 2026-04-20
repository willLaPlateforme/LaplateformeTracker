package com.laplateforme.tracker.view;

import com.laplateforme.tracker.controller.ImportExportController;
import com.laplateforme.tracker.controller.SearchController;
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
 * Liste des etudiants.
 *
 * NOUVEAUTES :
 * - Recherche avancee : ID, prenom, nom, email, age min/max, note min/max
 * - Panneau recherche avancee affichable/masquable
 * - Gestion des doublons visible via message d'erreur dans le formulaire
 */
public class StudentListView {

    private final StudentController      ctrl      = new StudentController();
    private final ImportExportController ioCtrl    = new ImportExportController();
    private final SearchController       searchCtrl = new SearchController();

    // Pagination
    private int    currentPage   = 1;
    private int    pageSize      = 10;
    private String searchKeyword = "";

    // Mode recherche avancee active
    private boolean advancedMode = false;

    private TableView<Student> table;
    private Label              pageLabel;

    // Champs de recherche avancee
    private TextField advIdField, advFirstField, advLastField, advEmailField;
    private TextField advMinAgeField, advMaxAgeField, advMinGradeField, advMaxGradeField;
    private VBox      advancedPanel;

    public void show(Stage stage) {

        // ── Colonnes du tableau ───────────────────────────────────────────────
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colId.setMaxWidth(50);

        TableColumn<Student, String>  colFirst = new TableColumn<>("Prenom");
        colFirst.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFirstName()));

        TableColumn<Student, String>  colLast  = new TableColumn<>("Nom");
        colLast.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLastName()));

        TableColumn<Student, Integer> colAge   = new TableColumn<>("Age");
        colAge.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getAge()).asObject());

        TableColumn<Student, Double>  colGrade = new TableColumn<>("Note");
        colGrade.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getGrade()).asObject());

        TableColumn<Student, String>  colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getEmail() == null ? "-" : c.getValue().getEmail()));

        table.getColumns().addAll(colId, colFirst, colLast, colAge, colGrade, colEmail);
        VBox.setVgrow(table, Priority.ALWAYS);

        // ── Recherche simple ─────────────────────────────────────────────────
        TextField searchField = new TextField();
        searchField.setPromptText("Rechercher par nom ou prenom...");
        searchField.setPrefWidth(200);
        searchField.textProperty().addListener((obs, old, val) -> {
            searchKeyword = val.trim();
            advancedMode  = false;
            currentPage   = 1;
            refresh();
        });

        Button clearBtn = new Button("X");
        clearBtn.setOnAction(e -> {
            searchField.clear();
            searchKeyword = "";
            advancedMode  = false;
            currentPage   = 1;
            resetAdvancedFields();
            refresh();
        });

        // ── Bouton bascule recherche avancee ─────────────────────────────────
        Button advBtn = new Button("Recherche avancee");
        advBtn.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white;");
        advBtn.setOnAction(e -> {
            boolean visible = !advancedPanel.isVisible();
            advancedPanel.setVisible(visible);
            advancedPanel.setManaged(visible);
            advBtn.setStyle(visible
                    ? "-fx-background-color: #4c1d95; -fx-text-fill: white;"
                    : "-fx-background-color: #7c3aed; -fx-text-fill: white;");
        });

        // ── Panneau recherche avancee ─────────────────────────────────────────
        advIdField      = field("ID exact", 70);
        advFirstField   = field("Prenom", 120);
        advLastField    = field("Nom",    120);
        advEmailField   = field("Email",  150);
        advMinAgeField  = field("Age min", 70);
        advMaxAgeField  = field("Age max", 70);
        advMinGradeField= field("Note min", 70);
        advMaxGradeField= field("Note max", 70);

        Button runAdvBtn   = new Button("Rechercher");
        Button clearAdvBtn = new Button("Reinitialiser");
        runAdvBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");

        runAdvBtn.setOnAction(e -> {
            advancedMode = true;
            currentPage  = 1;
            searchField.clear();
            searchKeyword = "";
            refresh();
        });
        clearAdvBtn.setOnAction(e -> {
            resetAdvancedFields();
            advancedMode  = false;
            currentPage   = 1;
            refresh();
        });

        HBox advRow1 = new HBox(8,
                lbl("ID :"), advIdField,
                lbl("Prenom :"), advFirstField,
                lbl("Nom :"), advLastField,
                lbl("Email :"), advEmailField);
        advRow1.setAlignment(Pos.CENTER_LEFT);

        HBox advRow2 = new HBox(8,
                lbl("Age min :"), advMinAgeField,
                lbl("Age max :"), advMaxAgeField,
                lbl("Note min :"), advMinGradeField,
                lbl("Note max :"), advMaxGradeField,
                runAdvBtn, clearAdvBtn);
        advRow2.setAlignment(Pos.CENTER_LEFT);

        advancedPanel = new VBox(6, advRow1, advRow2);
        advancedPanel.setPadding(new Insets(8, 0, 4, 0));
        advancedPanel.setStyle("-fx-background-color: #f5f3ff; -fx-padding: 8; -fx-border-radius: 6;");
        advancedPanel.setVisible(false);
        advancedPanel.setManaged(false);

        // ── Tri ───────────────────────────────────────────────────────────────
        ComboBox<String> sortBox = new ComboBox<>();
        sortBox.getItems().addAll("Nom", "Prenom", "Age", "Note", "ID");
        sortBox.setPromptText("Trier par...");

        Button sortBtn    = new Button("Tri ASC");
        final boolean[] asc = {true};
        sortBtn.setOnAction(e -> {
            if (sortBox.getValue() == null) return;
            String col = switch (sortBox.getValue()) {
                case "Prenom" -> "first_name";
                case "Age"    -> "age";
                case "Note"   -> "grade";
                case "ID"     -> "id";
                default       -> "last_name";
            };
            asc[0] = !asc[0];
            sortBtn.setText(asc[0] ? "Tri ASC" : "Tri DESC");
            List<Student> sorted = ctrl.getSortedStudents(col, asc[0] ? "ASC" : "DESC");
            table.setItems(FXCollections.observableArrayList(sorted));
            pageLabel.setText(sorted.size() + " etudiant(s)");
        });

        // ── CRUD ──────────────────────────────────────────────────────────────
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

        // ── Import / Export ───────────────────────────────────────────────────
        Button exportCsvBtn  = new Button("Export CSV");
        Button exportJsonBtn = new Button("Export JSON");
        Button importCsvBtn  = new Button("Import CSV");

        exportCsvBtn.setOnAction(e -> {
            FileChooser fc = chooser("CSV", "*.csv");
            File f = fc.showSaveDialog(stage);
            if (f != null) {
                String err = ioCtrl.exportCSV(ctrl.getAllStudents(), f.getAbsolutePath());
                if (err != null) alert(stage, err);
                else info(stage, "Export reussi : " + f.getName());
            }
        });

        exportJsonBtn.setOnAction(e -> {
            FileChooser fc = chooser("JSON", "*.json");
            File f = fc.showSaveDialog(stage);
            if (f != null) {
                String err = ioCtrl.exportJSON(ctrl.getAllStudents(), f.getAbsolutePath());
                if (err != null) alert(stage, err);
                else info(stage, "Export reussi : " + f.getName());
            }
        });

        importCsvBtn.setOnAction(e -> {
            FileChooser fc = chooser("CSV", "*.csv");
            File f = fc.showOpenDialog(stage);
            if (f != null) {
                List<Student> imported = ioCtrl.importCSV(f.getAbsolutePath());
                int ok = 0, skip = 0;
                for (Student s : imported) {
                    String err = ctrl.addStudent(s.getFirstName(), s.getLastName(),
                            String.valueOf(s.getAge()), String.valueOf(s.getGrade()),
                            s.getEmail());
                    if (err == null) ok++; else skip++;
                }
                info(stage, ok + " etudiant(s) importes." +
                        (skip > 0 ? "\n" + skip + " doublon(s) ignore(s)." : ""));
                refresh();
            }
        });

        // ── Pagination ────────────────────────────────────────────────────────
        Button prevBtn = new Button("< Precedent");
        Button nextBtn = new Button("Suivant >");
        pageLabel      = new Label();

        ComboBox<Integer> pageSizeBox = new ComboBox<>();
        pageSizeBox.getItems().addAll(5, 10, 20, 50);
        pageSizeBox.setValue(pageSize);
        pageSizeBox.setOnAction(e -> {
            pageSize = pageSizeBox.getValue(); currentPage = 1; refresh();
        });

        prevBtn.setOnAction(e -> { if (currentPage > 1) { currentPage--; refresh(); } });
        nextBtn.setOnAction(e -> {
            int total   = totalCount();
            int maxPage = Math.max(1, (int) Math.ceil((double) total / pageSize));
            if (currentPage < maxPage) { currentPage++; refresh(); }
        });

        // ── Assemblage ────────────────────────────────────────────────────────
        HBox searchBar = new HBox(8, searchField, clearBtn, advBtn, sortBox, sortBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        HBox crudBar = new HBox(8, addBtn, editBtn, deleteBtn, backBtn,
                new Separator(), exportCsvBtn, exportJsonBtn, importCsvBtn);
        crudBar.setAlignment(Pos.CENTER_LEFT);

        HBox pagBar = new HBox(10, prevBtn, pageLabel, nextBtn,
                new Label("  Par page :"), pageSizeBox);
        pagBar.setAlignment(Pos.CENTER_LEFT);

        VBox root = new VBox(8, searchBar, advancedPanel, crudBar, table, pagBar);
        root.setPadding(new Insets(16));
        VBox.setVgrow(table, Priority.ALWAYS);

        stage.setScene(new Scene(root, 900, 580));
        stage.setTitle("Liste des etudiants");
        stage.show();

        refresh();
    }

    // ── Logique de rafraichissement ───────────────────────────────────────────

    private void refresh() {
        if (advancedMode) {
            refreshAdvanced();
        } else if (!searchKeyword.isBlank()) {
            refreshSimple();
        } else {
            refreshAll();
        }
    }

    private void refreshAll() {
        int total   = ctrl.getTotalCount();
        int maxPage = Math.max(1, (int) Math.ceil((double) total / pageSize));
        if (currentPage > maxPage) currentPage = maxPage;
        table.setItems(FXCollections.observableArrayList(ctrl.getPage(currentPage, pageSize)));
        pageLabel.setText("Page " + currentPage + "/" + maxPage + " (" + total + " etudiants)");
    }

    private void refreshSimple() {
        List<Student> all = ctrl.searchStudents(searchKeyword);
        paginate(all);
    }

    private void refreshAdvanced() {
        Integer id       = parseId(advIdField.getText());
        String  fn       = blank(advFirstField.getText());
        String  ln       = blank(advLastField.getText());
        String  em       = blank(advEmailField.getText());
        Integer minAge   = parseInt(advMinAgeField.getText());
        Integer maxAge   = parseInt(advMaxAgeField.getText());
        Double  minGrade = parseDouble(advMinGradeField.getText());
        Double  maxGrade = parseDouble(advMaxGradeField.getText());

        List<Student> all = searchCtrl.advancedSearch(id, fn, ln, em, minAge, maxAge, minGrade, maxGrade);
        paginate(all);
    }

    private void paginate(List<Student> all) {
        int total   = all.size();
        int maxPage = Math.max(1, (int) Math.ceil((double) total / pageSize));
        if (currentPage > maxPage) currentPage = maxPage;
        int from = (currentPage - 1) * pageSize;
        int to   = Math.min(from + pageSize, all.size());
        List<Student> page = from < all.size() ? all.subList(from, to) : List.of();
        table.setItems(FXCollections.observableArrayList(page));
        pageLabel.setText("Page " + currentPage + "/" + maxPage + " (" + total + " resultats)");
    }

    private int totalCount() {
        if (advancedMode) {
            return searchCtrl.countAdvancedSearch(
                    parseId(advIdField.getText()),
                    blank(advFirstField.getText()), blank(advLastField.getText()),
                    blank(advEmailField.getText()),
                    parseInt(advMinAgeField.getText()), parseInt(advMaxAgeField.getText()),
                    parseDouble(advMinGradeField.getText()), parseDouble(advMaxGradeField.getText()));
        }
        if (!searchKeyword.isBlank()) return ctrl.searchStudents(searchKeyword).size();
        return ctrl.getTotalCount();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void resetAdvancedFields() {
        for (TextField f : new TextField[]{advIdField, advFirstField, advLastField,
                advEmailField, advMinAgeField, advMaxAgeField,
                advMinGradeField, advMaxGradeField}) {
            if (f != null) f.clear();
        }
    }

    private TextField field(String prompt, double width) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setPrefWidth(width);
        return f;
    }

    private Label lbl(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-font-size: 11px;");
        return l;
    }

    private FileChooser chooser(String desc, String ext) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(desc, ext));
        return fc;
    }

    private Integer parseId(String s)     { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; } }
    private Integer parseInt(String s)    { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return null; } }
    private Double  parseDouble(String s) { try { return Double.parseDouble(s.trim().replace(",",".")); } catch (Exception e) { return null; } }
    private String  blank(String s)       { return (s == null || s.isBlank()) ? null : s.trim(); }

    private void alert(Stage stage, String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
    private void info(Stage stage, String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}
