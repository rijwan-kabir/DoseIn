module com.momentum.dosein {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    opens com.momentum.dosein.controller to javafx.fxml;
    opens com.momentum.dosein.app        to javafx.fxml;

    exports com.momentum.dosein.app;
    exports com.momentum.dosein.controller;
    exports com.momentum.dosein.service;
    exports com.momentum.dosein.model;
}
