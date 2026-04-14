module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens laplateforme_tracker.view to javafx.fxml; /* controllers de FXML */
    exports laplateforme_tracker;
}
