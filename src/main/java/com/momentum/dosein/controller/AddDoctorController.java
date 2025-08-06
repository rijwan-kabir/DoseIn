package com.momentum.dosein.controller;

import com.momentum.dosein.model.DoctorContact;
import com.momentum.dosein.service.DoctorService;
import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AddDoctorController {

    @FXML private TextField nameField;
    @FXML private TextField specialtyField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField locationField;
    @FXML private TextArea  noteArea;

    private final DoctorService service = new DoctorService();

    @FXML
    private void handleAddEntry(ActionEvent e) {
        String name      = nameField.getText().trim();
        String spec      = specialtyField.getText().trim();
        String phone     = phoneField.getText().trim();
        String email     = emailField.getText().trim();
        String loc       = locationField.getText().trim();
        String note      = noteArea.getText().trim();

        if (name.isEmpty() || spec.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Name and Speciality are required.")
                    .showAndWait();
            return;
        }

        DoctorContact dc = new DoctorContact(name, spec, phone, email, loc, note);
        service.addContact(dc);

        // go back and refresh contacts
        handleCancel(e);
    }

    @FXML
    private void handleCancel(ActionEvent e) {
        try {
            Parent p = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/fxml/doctor_contacts.fxml")
            );
            Scene s = ((Node)e.getSource()).getScene();
            s.setRoot(p);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load Doctor Contacts.")
                    .showAndWait();
        }
    }

    // sidebar navigation stub methods, same as before...
    @FXML private void handleDashboard(ActionEvent e)       { navigate("/com/momentum/dosein/fxml/dashboard.fxml", e); }
    @FXML private void handleManageSchedule(ActionEvent e)  { navigate("/com/momentum/dosein/fxml/manage_schedule.fxml", e); }
    @FXML private void handleCostEstimator(ActionEvent e)  { navigate("/com/momentum/dosein/fxml/cost_estimator.fxml", e); }
    @FXML private void handleDoctorContacts(ActionEvent e) { navigate("/com/momentum/dosein/fxml/doctor_contacts.fxml", e); }
    @FXML private void handleEmergency(ActionEvent e)       { navigate("/com/momentum/dosein/fxml/emergency.fxml", e); }
    @FXML private void handleAboutUs(ActionEvent e)         { navigate("/com/momentum/dosein/fxml/about_us.fxml", e); }
    @FXML private void handleSignOut(ActionEvent e)         {
        Session.clear();
        navigate("/com/momentum/dosein/fxml/login.fxml", e);
    }

    private void navigate(String fxml, ActionEvent e) {
        try {
            Parent p = FXMLLoader.load(getClass().getResource(fxml));
            ((Node)e.getSource()).getScene().setRoot(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
