package com.momentum.dosein.controller;

import com.momentum.dosein.model.MedicineReminder;
import com.momentum.dosein.service.ReminderService;
import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManageScheduleController {

    @FXML
    private Button bilastinButton, napaButton, dopghButton;
    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, noteLabel;
    @FXML
    private HBox timesBox;

    private final ReminderService service = new ReminderService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

    // Map medicine key → list of reminders
    private Map<String, List<MedicineReminder>> grouped;
    private String selectedMedicine = null;

    @FXML
    private void initialize() {
        grouped = new LinkedHashMap<>();
        for (MedicineReminder r : service.getAllReminders()) {
            String key = r.getMedicineName() + " " + r.getDosage();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        // Set default data (Napa 250 mg as shown in the image)
        showDefaultData();
        
        // Update button visibility based on available medicines
        updateMedicineButtonsVisibility();
    }

    private void updateMedicineButtonsVisibility() {
        // Show/hide buttons based on available medicines
        bilastinButton.setVisible(grouped.containsKey("Bilastin 250 mg"));
        napaButton.setVisible(grouped.containsKey("Napa 250 mg"));
        dopghButton.setVisible(grouped.containsKey("Dopgh 250 mg"));
    }

    private void showDefaultData() {
        // Show default data as in the image
        nameLabel.setText("Napa 250 mg");
        startDateLabel.setText("25/07/25");
        endDateLabel.setText("25/07/26");
        noteLabel.setText("Take after eating something");
        
        // Clear and set default time buttons
        timesBox.getChildren().clear();
        Button btn1 = new Button("12:00 AM");
        btn1.getStyleClass().add("time-button");
        Button btn2 = new Button("1:00 AM");
        btn2.getStyleClass().add("time-button");
        Button btn3 = new Button("2:00 AM");
        btn3.getStyleClass().add("time-button");
        
        timesBox.getChildren().addAll(btn1, btn2, btn3);
        
        selectedMedicine = "Napa 250 mg";
    }

    @FXML
    private void handleMedicineSelection(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        String medicineKey = clickedButton.getText();
        
        // Reset all medicine button styles
        resetMedicineButtonStyles();
        
        // Highlight selected button
        clickedButton.getStyleClass().add("medicine-button-selected");
        
        // Show details for selected medicine
        if (grouped.containsKey(medicineKey)) {
            showDetails(medicineKey);
        }
        
        selectedMedicine = medicineKey;
    }

    private void resetMedicineButtonStyles() {
        bilastinButton.getStyleClass().removeAll("medicine-button-selected");
        napaButton.getStyleClass().removeAll("medicine-button-selected");
        dopghButton.getStyleClass().removeAll("medicine-button-selected");
    }

    private void showDetails(String key) {
        List<MedicineReminder> items = grouped.get(key);
        if (items.isEmpty()) return;

        MedicineReminder r0 = items.get(0);
        nameLabel.setText(r0.getMedicineName() + " " + r0.getDosage());
        startDateLabel.setText(r0.getStartDate().format(dateFmt));
        endDateLabel.setText(r0.getEndDate().format(dateFmt));
        noteLabel.setText(r0.getNote());

        timesBox.getChildren().clear();
        for (MedicineReminder r : items) {
            Button btn = new Button(r.getTime().format(timeFmt));
            btn.getStyleClass().add("time-button");
            timesBox.getChildren().add(btn);
        }
    }

    @FXML
    private void handleDelete(ActionEvent e) {
        if (selectedMedicine == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a medicine first.")
                    .showAndWait();
            return;
        }
        
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Delete Medicine");
        confirmAlert.setHeaderText("Delete Medicine Schedule");
        confirmAlert.setContentText("Are you sure you want to delete the schedule for " + selectedMedicine + "?");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.deleteByMedicine(selectedMedicine);
            initialize(); // Refresh the display
            new Alert(Alert.AlertType.INFORMATION, "Medicine schedule deleted successfully.")
                    .showAndWait();
        }
    }

    @FXML
    private void handleDashboard(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/dashboard.fxml", e);
    }

    @FXML
    private void handleManageSchedule(ActionEvent e) { /* noop */ }

    @FXML
    private void handleDoctorContacts(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/doctor_contacts.fxml", e);
    }

    @FXML
    private void handleEmergency(ActionEvent e) {  
        navigate("/com/momentum/dosein/fxml/emergency.fxml", e);
    }

    @FXML
    private void handleAboutUs(ActionEvent e) { 
        navigate("/com/momentum/dosein/fxml/about_us.fxml", e); 
    }

    @FXML
    private void handleSignOut(ActionEvent e) {
        Session.clear();
        navigate("/com/momentum/dosein/fxml/login.fxml", e);
    }

    private void navigate(String fxmlPath, ActionEvent e) {
        try {
            Parent p = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene s = ((Node) e.getSource()).getScene();
            s.setRoot(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
