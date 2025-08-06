package com.momentum.dosein.controller;

import com.momentum.dosein.model.DoctorContact;
import com.momentum.dosein.service.DoctorService;
import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.List;

public class DoctorContactsController {

    @FXML private VBox contactsVBox;

    private final DoctorService service = new DoctorService();

    @FXML
    public void initialize() {
        // load persisted contacts
        List<DoctorContact> all = service.getAllContacts();
        loadContacts(all);
    }

    private void loadContacts(List<DoctorContact> contacts) {
        contactsVBox.getChildren().clear();

        for (DoctorContact dc : contacts) {
            // Create doctor contact bar
            HBox contactBar = new HBox();
            contactBar.setAlignment(Pos.CENTER_LEFT);
            contactBar.setSpacing(10);
            contactBar.setStyle("-fx-padding: 0 15;");
            contactBar.getStyleClass().add("contact-bar");

            // Doctor name on the left
            Label nameLabel = new Label(dc.getName());
            nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

            // Spacer to push phone to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // Phone number on the right
            Label phoneLabel = new Label(dc.getPhone());
            phoneLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

            contactBar.getChildren().addAll(nameLabel, spacer, phoneLabel);

            // Add click handler
            contactBar.setOnMouseClicked(event -> {
                navigateToDoctorInfo(dc);
            });

            // Add to VBox
            contactsVBox.getChildren().add(contactBar);
        }
    }

    private void navigateToDoctorInfo(DoctorContact doctor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/momentum/dosein/fxml/doctor_info.fxml"));
            Parent root = loader.load();

            // Get the controller and set the doctor
            DoctorInfoController controller = loader.getController();
            controller.setDoctor(doctor);

            Scene scene = contactsVBox.getScene();
            scene.setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load Doctor Information screen.").showAndWait();
        }
    }

    @FXML
    private void handleAdd(ActionEvent e) {
        try {
            Parent p = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/fxml/add_doctor.fxml")
            );
            Scene s = ((Node)e.getSource()).getScene();
            s.setRoot(p);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load Add Doctor screen.").showAndWait();
        }
    }

    // sidebar navigation stubs:
    @FXML private void handleDashboard(ActionEvent e)       { navigate("/com/momentum/dosein/fxml/dashboard.fxml", e); }
    @FXML private void handleManageSchedule(ActionEvent e)  { navigate("/com/momentum/dosein/fxml/manage_schedule.fxml", e); }
    @FXML private void handleCostEstimator(ActionEvent e)  { navigate("/com/momentum/dosein/fxml/cost_estimator.fxml", e); }
    @FXML private void handleDoctorContacts(ActionEvent e) { /* noop */ }
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
