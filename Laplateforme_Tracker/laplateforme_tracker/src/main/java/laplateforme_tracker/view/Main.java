package laplateforme_tracker.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Point d'entrée de l'application JavaFX.
 * Lance l'écran de connexion au démarrage.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Chargement du fichier FXML de la page de connexion
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));

        primaryStage.setTitle("Gestion Étudiants");
        primaryStage.setScene(new Scene(root, 450, 380));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}