package com.momentum.dosein.controller;

import com.momentum.dosein.model.MedicineReminder;
import com.momentum.dosein.model.User;
import com.momentum.dosein.service.ReminderService;
import com.momentum.dosein.util.Session;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardController {

    @FXML private Label greetingLabel;
    @FXML private Label sloganLabel;
    @FXML private Label timeLabel;
    @FXML private VBox   scheduleContainer;

    private final ReminderService reminderService = new ReminderService();

    @FXML
    public void initialize() {
        // 1. Greet user
        User current = Session.getCurrentUser();
        greetingLabel.setText("Hello, " +
                (current != null ? current.getUsername() : "User") + "!");
        sloganLabel.setText("Your Health Matters – Stay on Track!");

        // 2. Start clock
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh : mm : ss a");
        Timeline clock = new Timeline(
                new KeyFrame(Duration.ZERO,
                        e -> timeLabel.setText(LocalTime.now().format(dtf))),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        // 3. Load existing reminders
        loadSchedule();
    }

    private void loadSchedule() {
        scheduleContainer.getChildren().clear();
        List<MedicineReminder> list = reminderService.getAllReminders();
        DateTimeFormatter tfmt = DateTimeFormatter.ofPattern("h:mm a");

        for (MedicineReminder r : list) {
            HBox card = new HBox();
            card.getStyleClass().add("schedule-card");
            card.setSpacing(10);

            VBox texts = new VBox(5);
            Label t = new Label(r.getTime().format(tfmt));
            t.getStyleClass().add("schedule-time");
            Label title = new Label(r.getMedicineName() + " " + r.getDosage());
            title.getStyleClass().add("schedule-title");
            Label note = new Label(r.getNote());
            note.getStyleClass().add("schedule-note");

            texts.getChildren().addAll(t, title, note);
            card.getChildren().add(texts);
            scheduleContainer.getChildren().add(card);
        }
    }

    // --- Navigation stubs ---
    @FXML private void handleDashboard(ActionEvent e)     { /* already here */ }
    @FXML private void handleManageSchedule(ActionEvent e){ /* TODO */ }
    @FXML private void handleDoctorContacts(ActionEvent e){ /* TODO */ }
    @FXML private void handleEmergency(ActionEvent e)     { /* TODO */ }
    @FXML private void handleAboutUs(ActionEvent e)       { /* TODO */ }
    @FXML private void handleSetReminder(ActionEvent e)   { /* TODO */ }

    @FXML
    private void handleSignOut(ActionEvent event) {
        Session.clear();
        try {
            Parent login = FXMLLoader.load(
                    getClass().getResource("/com/momentum/dosein/fxml/login.fxml")
            );
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(login, 800, 600));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
