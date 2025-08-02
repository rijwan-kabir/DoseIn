package com.momentum.dosein.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DoseInApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(
                getClass().getResource("/com/momentum/dosein/fxml/login.fxml")
        );
        primaryStage.setTitle("DoseIn");
        primaryStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/com/momentum/dosein/images/logo.png"))
        );
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
