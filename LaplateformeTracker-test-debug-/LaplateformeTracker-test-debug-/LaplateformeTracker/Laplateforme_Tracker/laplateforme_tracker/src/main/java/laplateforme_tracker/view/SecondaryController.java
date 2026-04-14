package laplateforme_tracker.view;

import java.io.IOException;

import javafx.fxml.FXML;
import laplateforme_tracker.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}