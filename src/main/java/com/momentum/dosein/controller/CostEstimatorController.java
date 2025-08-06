package com.momentum.dosein.controller;

import com.momentum.dosein.model.CartItem;
import com.momentum.dosein.model.Medicine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CostEstimatorController {

    @FXML private TextField searchField;
    @FXML private ListView<Medicine> medicineListView;
    @FXML private TableView<CartItem> cartTableView;
    @FXML private TableColumn<CartItem, String> medicineNameColumn;
    @FXML private TableColumn<CartItem, Integer> quantityColumn;
    @FXML private TableColumn<CartItem, Double> priceColumn;
    @FXML private TableColumn<CartItem, Double> totalColumn;
    @FXML private Label totalCostLabel;
    @FXML private Button addToCartButton;
    @FXML private Button removeFromCartButton;
    @FXML private Button clearCartButton;
    @FXML private Spinner<Integer> quantitySpinner;

    private ObservableList<Medicine> allMedicines;
    private ObservableList<Medicine> filteredMedicines;
    private ObservableList<CartItem> cartItems;

    @FXML
    public void initialize() {
        // Load Poppins font
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Poppins-Bold.ttf"), 14);

        // Initialize collections
        allMedicines = FXCollections.observableArrayList();
        filteredMedicines = FXCollections.observableArrayList();
        cartItems = FXCollections.observableArrayList();

        // Set up table columns
        medicineNameColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getMedicine().getName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMedicine().getPrice()).asObject());
        totalColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotalPrice()).asObject());

        // Format price columns
        priceColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText("$" + String.format("%.2f", price));
                }
            }
        });

        totalColumn.setCellFactory(column -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText("$" + String.format("%.2f", total));
                }
            }
        });

        // Set up ListView
        medicineListView.setItems(filteredMedicines);
        medicineListView.setCellFactory(listView -> new ListCell<Medicine>() {
            @Override
            protected void updateItem(Medicine medicine, boolean empty) {
                super.updateItem(medicine, empty);
                if (empty || medicine == null) {
                    setText(null);
                } else {
                    setText(medicine.getName() + " - $" + String.format("%.2f", medicine.getPrice()));
                }
            }
        });

        // Set up TableView
        cartTableView.setItems(cartItems);

        // Set up quantity spinner
        quantitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Set up search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterMedicines(newValue));

        // Load medicines from file
        loadMedicinesFromFile();

        // Update total cost when cart changes
        cartItems.addListener((javafx.collections.ListChangeListener<CartItem>) c -> updateTotalCost());

        // Set up button states
        updateButtonStates();
        medicineListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            updateButtonStates());
        cartTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
            updateButtonStates());
    }

    private void loadMedicinesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("medicines.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    allMedicines.add(new Medicine(name, price));
                }
            }
            filteredMedicines.addAll(allMedicines);
        } catch (IOException e) {
            showAlert("Error", "Could not load medicines from file: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price format in medicines file: " + e.getMessage());
        }
    }

    private void filterMedicines(String searchText) {
        filteredMedicines.clear();
        if (searchText == null || searchText.trim().isEmpty()) {
            filteredMedicines.addAll(allMedicines);
        } else {
            String lowerCaseFilter = searchText.toLowerCase();
            for (Medicine medicine : allMedicines) {
                if (medicine.getName().toLowerCase().contains(lowerCaseFilter)) {
                    filteredMedicines.add(medicine);
                }
            }
        }
    }

    @FXML
    private void handleAddToCart() {
        Medicine selectedMedicine = medicineListView.getSelectionModel().getSelectedItem();
        if (selectedMedicine == null) {
            showAlert("No Selection", "Please select a medicine to add to cart.");
            return;
        }

        int quantity = quantitySpinner.getValue();
        
        // Check if medicine is already in cart
        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getMedicine().equals(selectedMedicine))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartTableView.refresh();
        } else {
            // Add new item
            cartItems.add(new CartItem(selectedMedicine, quantity));
        }

        // Reset quantity spinner
        quantitySpinner.getValueFactory().setValue(1);
    }

    @FXML
    private void handleRemoveFromCart() {
        CartItem selectedItem = cartTableView.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("No Selection", "Please select an item to remove from cart.");
            return;
        }

        cartItems.remove(selectedItem);
    }

    @FXML
    private void handleClearCart() {
        if (!cartItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Clear Cart");
            alert.setHeaderText("Clear entire cart?");
            alert.setContentText("This will remove all items from your cart.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                cartItems.clear();
            }
        }
    }

    private void updateTotalCost() {
        double total = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        totalCostLabel.setText("Total: $" + String.format("%.2f", total));
        totalCostLabel.setFont(Font.font("Poppins", FontWeight.BOLD, 18));
    }

    private void updateButtonStates() {
        addToCartButton.setDisable(medicineListView.getSelectionModel().getSelectedItem() == null);
        removeFromCartButton.setDisable(cartTableView.getSelectionModel().getSelectedItem() == null);
        clearCartButton.setDisable(cartItems.isEmpty());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Navigation methods (same pattern as other controllers)
    @FXML
    private void handleDashboard(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/dashboard.fxml", e);
    }

    @FXML
    private void handleManageSchedule(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/manage_schedule.fxml", e);
    }

    @FXML
    private void handleCostEstimator(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/cost_estimator.fxml", e);
    }

    @FXML
    private void handleDoctorContacts(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/doctor_contacts.fxml", e);
    }

    @FXML
    private void handleEmergency(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/emergency.fxml", e);
    }

    @FXML
    private void handleAboutUs(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/about_us.fxml", e);
    }

    @FXML
    private void handleSetReminder(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/set_reminder.fxml", e);
    }

    @FXML
    private void handleSignOut(ActionEvent e) {
        swapRoot("/com/momentum/dosein/fxml/login.fxml", e);
    }

    private void swapRoot(String fxmlPath, ActionEvent e) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = ((Node) e.getSource()).getScene();
            scene.setRoot(pane);
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Error", "Could not load screen: " + fxmlPath);
        }
    }
}