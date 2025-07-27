package com.momentum.dosein.controller;

import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;

import java.io.IOException;

public class AboutUsController {

    @FXML private void handleDashboard(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/dashboard.fxml", e);
    }
    @FXML private void handleManageSchedule(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/manage_schedule.fxml", e);
    }
    @FXML private void handleDoctorContacts(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/doctor_contacts.fxml", e);
    }
    @FXML private void handleEmergency(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/emergency.fxml", e);
    }
    @FXML private void handleAboutUs(ActionEvent e) {
        // no-op (already here)
    }
    @FXML private void handleSignOut(ActionEvent e) {
        Session.clear();
        swapRoot("/com/momentum/dosein/fxml/login.fxml", e);
    }

    private void swapRoot(String fxmlPath, ActionEvent e) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node)e.getSource()).getScene();
            scene.setRoot(pane);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load: " + fxmlPath).showAndWait();
        }
    }
}
