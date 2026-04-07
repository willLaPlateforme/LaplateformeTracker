module laplateforme_tracker {
    requires javafx.controls;
    requires javafx.fxml;

    opens laplateforme_tracker to javafx.fxml;
    exports laplateforme_tracker;
}
