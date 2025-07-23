package com.momentum.dosein.controller;

import com.momentum.dosein.model.User;
import com.momentum.dosein.service.UserService;
import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final UserService userService = new UserService();

    @FXML
    private void handleSignIn(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        User user = userService.login(username, password);
        if (user != null) {
            // 1) Save into session
            Session.setCurrentUser(user);

            // 2) Load Dashboard scene
            try {
                Parent dashboardRoot = FXMLLoader.load(
                        getClass().getResource("/com/momentum/dosein/fxml/dashboard.fxml")
                );
                Scene dashboardScene = new Scene(dashboardRoot, 800, 600);

                // 3) Swap scenes on the current window
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setScene(dashboardScene);
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Could not load the dashboard screen.")
                        .showAndWait();
            }
        } else {
            new Alert(Alert.AlertType.ERROR,
                    "Invalid username or password.")
                    .showAndWait();
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        try {
            Parent signupRoot = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/fxml/signup.fxml")
            );
            Scene signupScene = new Scene(signupRoot, 800, 600);
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(signupScene);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load the sign-up screen.")
                    .showAndWait();
        }
    }
}
