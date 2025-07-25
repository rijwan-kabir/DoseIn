package com.momentum.dosein.controller;

import com.momentum.dosein.model.MedicineReminder;
import com.momentum.dosein.service.ReminderService;
import com.momentum.dosein.util.Session;               // ← add this
import javafx.collections.ObservableList;
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

    @FXML private ListView<String> alertsList;
    @FXML private Label nameLabel, startDateLabel, endDateLabel, noteLabel;
    @FXML private HBox timesBox;

    private final ReminderService service = new ReminderService();
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yy");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("h:mm a");

    // Map medicine key → list of reminders
    private Map<String, List<MedicineReminder>> grouped;

    @FXML
    private void initialize() {
        grouped = new LinkedHashMap<>();
        for (MedicineReminder r : service.getAllReminders()) {
            String key = r.getMedicineName() + " " + r.getDosage();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(r);
        }

        alertsList.getItems().setAll(grouped.keySet());

        alertsList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldKey, newKey) -> {
                    if (newKey != null) showDetails(newKey);
                });
    }

    private void showDetails(String key) {
        List<MedicineReminder> items = grouped.get(key);
        if (items.isEmpty()) return;

        MedicineReminder r0 = items.get(0);
        nameLabel.setText(r0.getMedicineName() + " " + r0.getDosage());
        startDateLabel.setText(r0.getStartDate().format(dateFmt));
        endDateLabel  .setText(r0.getEndDate().format(dateFmt));
        noteLabel     .setText(r0.getNote());

        timesBox.getChildren().clear();
        for (MedicineReminder r : items) {
            Button btn = new Button(r.getTime().format(timeFmt));
            btn.getStyleClass().add("schedule-button");
            timesBox.getChildren().add(btn);
        }
    }

    @FXML
    private void handleDelete(ActionEvent e) {
        String key = alertsList.getSelectionModel().getSelectedItem();
        if (key == null) {
            new Alert(Alert.AlertType.WARNING, "Select an alert first.")
                    .showAndWait();
            return;
        }
        service.deleteByMedicine(key);
        initialize();
    }

    @FXML
    private void handleDashboard(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/dashboard.fxml", e);
    }

    @FXML private void handleManageSchedule(ActionEvent e) { /* noop */ }
    @FXML private void handleDoctorContacts(ActionEvent e) { /* TODO */ }
    @FXML private void handleEmergency(ActionEvent e)    { /* TODO */ }
    @FXML private void handleAboutUs(ActionEvent e)      { /* TODO */ }

    @FXML
    private void handleSignOut(ActionEvent e) {
        Session.clear();                              // now recognized
        navigate("/com/momentum/dosein/fxml/login.fxml", e);
    }

    private void navigate(String fxmlPath, ActionEvent e) {
        try {
            Parent p = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene s = ((Node)e.getSource()).getScene();
            s.setRoot(p);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
