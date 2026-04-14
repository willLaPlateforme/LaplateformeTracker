package com.laplateforme.tracker;

import com.laplateforme.tracker.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        new LoginView().show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
