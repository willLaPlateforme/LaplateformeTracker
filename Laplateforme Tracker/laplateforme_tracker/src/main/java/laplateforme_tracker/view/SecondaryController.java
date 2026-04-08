package laplateforme_tracker.view;

import java.io.IOException;

import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        MainView.setRoot("primary");
    }
}