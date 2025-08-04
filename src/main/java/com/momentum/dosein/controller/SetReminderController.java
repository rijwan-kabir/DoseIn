package com.momentum.dosein.controller;

import com.momentum.dosein.model.MedicineReminder;
import com.momentum.dosein.service.ReminderService;
import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SetReminderController {

    @FXML private TextField    nameField;
    @FXML private DatePicker   startDatePicker, endDatePicker;
    @FXML private Spinner<Integer> hourSpinner, minuteSpinner;
    @FXML private RadioButton  amRadio, pmRadio;
    @FXML private ListView<String> timesList;
    @FXML private TextField    noteField;

    private final ReminderService reminderService = new ReminderService();
    private final DateTimeFormatter displayFmt =
            DateTimeFormatter.ofPattern("h:mm a");

    @FXML
    private void initialize() {
        // Make spinners editable so user can type
        hourSpinner.setEditable(true);
        minuteSpinner.setEditable(true);

        // Spinner ranges
        hourSpinner.setValueFactory(new IntegerSpinnerValueFactory(1, 12, 12));
        minuteSpinner.setValueFactory(new IntegerSpinnerValueFactory(0, 59, 0));

        // Group AM/PM toggles
        var tg = new javafx.scene.control.ToggleGroup();
        amRadio.setToggleGroup(tg);
        pmRadio.setToggleGroup(tg);
        amRadio.setSelected(true);
    }

    @FXML
    private void handleAddTime(ActionEvent e) {
        int hour = hourSpinner.getValue() % 12;
        if (pmRadio.isSelected()) hour += 12;
        LocalTime t = LocalTime.of(hour, minuteSpinner.getValue());
        timesList.getItems().add(t.format(displayFmt));
    }

    @FXML
    private void handleRemoveTime(ActionEvent e) {
        var sel = timesList.getSelectionModel().getSelectedIndices();
        sel.sorted((a, b) -> b - a)
                .forEach(i -> timesList.getItems().remove((int) i));
    }

    @FXML
    private void handleSetReminder(ActionEvent e) {
        String name = nameField.getText().trim();
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();

        if (name.isEmpty() || timesList.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Please enter a name and at least one alert time.")
                    .showAndWait();
            return;
        }

        if (start == null || end == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Please select both start and end dates.")
                    .showAndWait();
            return;
        }

        if (end.isBefore(start)) {
            new Alert(Alert.AlertType.WARNING,
                    "End date cannot be before start date.")
                    .showAndWait();
            return;
        }

        String note = noteField.getText().trim();

        for (String ts : timesList.getItems()) {
            LocalTime lt = LocalTime.parse(ts, displayFmt);
            MedicineReminder r = new MedicineReminder(name, "", start, end, lt, note);
            reminderService.addReminder(r);
        }

        new Alert(Alert.AlertType.INFORMATION,
                "Reminder(s) saved!").showAndWait();
        navigateToDashboard(e);
    }

    /** Cancel button → back to Dashboard */
    @FXML
    private void handleCancel(ActionEvent e) {
        navigateToDashboard(e);
    }

    /** Dashboard nav (button or logo click) → back to Dashboard */
    @FXML
    private void handleDashboard(Event e) {
        navigateToDashboard(e);
    }

    private void navigateToDashboard(Event event) {
        try {
            Parent dash = FXMLLoader.load(
                    getClass().getResource(
                            "/com/momentum/dosein/fxml/dashboard.fxml"
                    )
            );
            Scene scene = ((Node)event.getSource()).getScene();
            scene.setRoot(dash);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "Could not load Dashboard.").showAndWait();
        }
    }

    // Navigation methods for remaining nav buttons:
    @FXML
    private void handleManageSchedule(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/manage_schedule.fxml", e);
    }

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

    private void navigate(String fxmlPath, ActionEvent e) {
        try {
            Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node) e.getSource()).getScene();
            scene.setRoot(page);
        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not load page.").showAndWait();
        }
    }

    @FXML
    private void handleSignOut(Event e) {
        Session.clear();
        try {
            Parent login = FXMLLoader.load(
                    getClass().getResource(
                            "/com/momentum/dosein/fxml/login.fxml"
                    )
            );
            Scene scene = ((Node)e.getSource()).getScene();
            scene.setRoot(login);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
