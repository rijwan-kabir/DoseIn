package com.momentum.dosein.controller;

import com.momentum.dosein.model.User;
import com.momentum.dosein.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignupController {
    @FXML private TextField    usernameField;
    @FXML private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields.")
                    .showAndWait();
            return;
        }

        boolean success = userService.register(new User(username, password, ""));
        if (success) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Registration successful! Please sign in.")
                    .showAndWait();
            // go back to login
            goToLoginScene(event);
        } else {
            new Alert(Alert.AlertType.ERROR,
                    "Username already exists.")
                    .showAndWait();
        }
    }

    @FXML
    private void handleGoToLogin(ActionEvent event) {
        goToLoginScene(event);
    }

    private void goToLoginScene(ActionEvent event) {
        try {
            Parent loginRoot = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/fxml/login.fxml")
            );
            Scene scene = new Scene(loginRoot, 800, 600);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load login screen.").showAndWait();
        }
    }
}
