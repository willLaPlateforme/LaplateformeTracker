package laplateforme_tracker;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App_View.setRoot("secondary");
    }
}
