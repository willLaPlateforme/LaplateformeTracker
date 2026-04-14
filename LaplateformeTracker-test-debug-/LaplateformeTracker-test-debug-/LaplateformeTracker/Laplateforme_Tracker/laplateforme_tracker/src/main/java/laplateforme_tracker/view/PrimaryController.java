package laplateforme_tracker.view;

import java.io.IOException;
import laplateforme_tracker.App;

import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
