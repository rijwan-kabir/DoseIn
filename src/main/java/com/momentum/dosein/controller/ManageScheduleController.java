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
import javafx.scene.layout.VBox;
import javafx.css.PseudoClass;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ManageScheduleController {

    @FXML
    private VBox medicineButtonsContainer;
    @FXML
    private Label emptyStateLabel;
    @FXML
    private Label nameLabel, startDateLabel, endDateLabel, noteLabel;
    @FXML
    private HBox timesBox;

    private final ReminderService service = new ReminderService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");
    private final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    // Map medicine key → list of reminders
    private Map<String, List<MedicineReminder>> grouped;
    private String selectedMedicine = null;
    private Button currentSelectedButton = null;

    @FXML
    private void initialize() {
        loadReminders();
        createMedicineButtons();
        updateDisplay();
    }

    private void loadReminders() {
        grouped = new LinkedHashMap<>();
        List<MedicineReminder> allReminders = service.getAllReminders();

        for (MedicineReminder r : allReminders) {
            String key = r.getMedicineName();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }
    }

    private void createMedicineButtons() {
        medicineButtonsContainer.getChildren().clear();

        if (grouped.isEmpty()) {
            emptyStateLabel.setVisible(true);
            return;
        }

        emptyStateLabel.setVisible(false);

        for (String medicineName : grouped.keySet()) {
            Button medicineButton = new Button(medicineName);
            medicineButton.getStyleClass().add("medicine-button");
            medicineButton.setPrefWidth(190);
            medicineButton.setOnAction(this::handleMedicineSelection);

            // Set initial selected state if this is the currently selected medicine
            if (medicineName.equals(selectedMedicine)) {
                medicineButton.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
                currentSelectedButton = medicineButton;
            }

            medicineButtonsContainer.getChildren().add(medicineButton);
        }
    }

    private void updateDisplay() {
        if (grouped.isEmpty()) {
            showEmptyState();
        } else {
            showDefaultState();
        }
    }

    private void showEmptyState() {
        nameLabel.setText("No reminders set");
        startDateLabel.setText("-");
        endDateLabel.setText("-");
        noteLabel.setText("Go to Set Reminder to add medicines");
        timesBox.getChildren().clear();
        selectedMedicine = null;
        currentSelectedButton = null;
    }

    private void showDefaultState() {
        nameLabel.setText("No medicine selected");
        startDateLabel.setText("-");
        endDateLabel.setText("-");
        noteLabel.setText("Select a medicine from the list");
        timesBox.getChildren().clear();

        // Keep the selected medicine if it still exists
        if (selectedMedicine != null && grouped.containsKey(selectedMedicine)) {
            showDetails(selectedMedicine);
        } else {
            selectedMedicine = null;
            currentSelectedButton = null;
        }
    }

    @FXML
    private void handleMedicineSelection(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        String medicineKey = clickedButton.getText();

        // If clicking the already selected button, do nothing
        if (medicineKey.equals(selectedMedicine)) {
            return;
        }

        // Remove selection from all buttons
        medicineButtonsContainer.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
            }
        });

        // Set selected state for clicked button
        clickedButton.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
        currentSelectedButton = clickedButton;
        selectedMedicine = medicineKey;

        // Show details for selected medicine
        showDetails(medicineKey);
    }

    private void showDetails(String medicineKey) {
        List<MedicineReminder> items = grouped.get(medicineKey);
        if (items == null || items.isEmpty()) return;

        MedicineReminder firstReminder = items.get(0);

        // Set basic info
        nameLabel.setText(firstReminder.getMedicineName());

        // Format dates - handle null dates gracefully
        if (firstReminder.getStartDate() != null) {
            startDateLabel.setText(firstReminder.getStartDate().format(dateFmt));
        } else {
            startDateLabel.setText("Not set");
        }

        if (firstReminder.getEndDate() != null) {
            endDateLabel.setText(firstReminder.getEndDate().format(dateFmt));
        } else {
            endDateLabel.setText("Not set");
        }

        // Set note
        String note = firstReminder.getNote();
        if (note != null && !note.trim().isEmpty()) {
            noteLabel.setText(note);
        } else {
            noteLabel.setText("No additional information");
        }

        // Create time buttons for all reminders of this medicine
        timesBox.getChildren().clear();
        for (MedicineReminder reminder : items) {
            if (reminder.getTime() != null) {
                Button timeBtn = new Button(reminder.getTime().format(timeFmt));
                timeBtn.getStyleClass().add("time-button");
                timesBox.getChildren().add(timeBtn);
            }
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
        confirmAlert.setContentText("Are you sure you want to delete all reminders for " + selectedMedicine + "?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.deleteByMedicine(selectedMedicine + " ");
            initialize();

            new Alert(Alert.AlertType.INFORMATION,
                    "All reminders for " + selectedMedicine + " have been deleted successfully.")
                    .showAndWait();
        }
    }

    // Navigation methods remain the same
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