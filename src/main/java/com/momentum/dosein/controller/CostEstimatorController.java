package com.momentum.dosein.controller;

import com.momentum.dosein.util.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.css.PseudoClass;

import java.io.IOException;
import java.util.*;

public class CostEstimatorController {

    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private VBox searchResultsContainer;
    @FXML
    private Label emptySearchLabel;
    @FXML
    private VBox cartItemsGrid;
    @FXML
    private Label cartEmptyLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private VBox actionButtonsContainer;
    @FXML
    private Button clearCartButton;
    @FXML
    private Button removeItemButton;

    private final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    // Sample medicine data with prices
    private final Map<String, Double> medicinePrices = new HashMap<>();
    private final List<String> cartItems = new ArrayList<>();
    private String selectedCartItem = null;
    private Button currentSelectedCartButton = null;

    @FXML
    private void initialize() {
        // Initialize sample medicine prices
        initializeMedicinePrices();
        
        // Set initial state
        updateButtonVisibility();
        updateTotal();
    }

    private void initializeMedicinePrices() {
        medicinePrices.put("Paracetamol 500mg", 5.99);
        medicinePrices.put("Ibuprofen 400mg", 7.50);
        medicinePrices.put("Aspirin 100mg", 4.25);
        medicinePrices.put("Omeprazole 20mg", 12.75);
        medicinePrices.put("Cetirizine 10mg", 8.99);
        medicinePrices.put("Metformin 500mg", 15.50);
        medicinePrices.put("Amlodipine 5mg", 18.25);
        medicinePrices.put("Lisinopril 10mg", 22.00);
        medicinePrices.put("Atorvastatin 20mg", 25.75);
        medicinePrices.put("Metoprolol 50mg", 16.80);
        medicinePrices.put("Losartan 50mg", 19.45);
        medicinePrices.put("Sertraline 50mg", 28.90);
        medicinePrices.put("Fluoxetine 20mg", 24.60);
        medicinePrices.put("Diazepam 5mg", 32.15);
        medicinePrices.put("Alprazolam 0.5mg", 35.80);
    }

    @FXML
    private void handleSearch(ActionEvent e) {
        String searchTerm = searchField.getText().trim().toLowerCase();
        
        if (searchTerm.isEmpty()) {
            showAlert("Search Error", "Please enter a medicine name to search.");
            return;
        }

        // Clear previous results
        searchResultsContainer.getChildren().clear();
        emptySearchLabel.setVisible(true);

        // Search for medicines
        List<String> matchingMedicines = new ArrayList<>();
        for (String medicine : medicinePrices.keySet()) {
            if (medicine.toLowerCase().contains(searchTerm)) {
                matchingMedicines.add(medicine);
            }
        }

        if (matchingMedicines.isEmpty()) {
            emptySearchLabel.setText("No medicines found matching '" + searchTerm + "'");
            emptySearchLabel.setVisible(true);
        } else {
            emptySearchLabel.setVisible(false);
            
            for (String medicine : matchingMedicines) {
                Button medicineButton = new Button(medicine + " - $" + String.format("%.2f", medicinePrices.get(medicine)));
                medicineButton.getStyleClass().add("medicine-item-button");
                medicineButton.setPrefWidth(230);
                medicineButton.setOnAction(this::handleAddToCart);
                
                searchResultsContainer.getChildren().add(medicineButton);
            }
        }
    }

    @FXML
    private void handleAddToCart(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        String buttonText = clickedButton.getText();
        
        // Extract medicine name (remove price part)
        String medicineName = buttonText.substring(0, buttonText.lastIndexOf(" - $"));
        
        if (cartItems.contains(medicineName)) {
            showAlert("Already in Cart", medicineName + " is already in your cart.");
            return;
        }

        // Add to cart
        cartItems.add(medicineName);
        updateCartDisplay();
        updateTotal();
        updateButtonVisibility();
        
        showAlert("Added to Cart", medicineName + " has been added to your cart.");
    }

    @FXML
    private void handleRemoveItem(ActionEvent e) {
        if (selectedCartItem == null) {
            showAlert("No Selection", "Please select an item to remove from the cart.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Remove Item");
        confirmAlert.setHeaderText("Remove from Cart");
        confirmAlert.setContentText("Are you sure you want to remove " + selectedCartItem + " from your cart?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartItems.remove(selectedCartItem);
            selectedCartItem = null;
            currentSelectedCartButton = null;
            updateCartDisplay();
            updateTotal();
            updateButtonVisibility();
            
            showAlert("Item Removed", "Item has been removed from your cart.");
        }
    }

    @FXML
    private void handleClearCart(ActionEvent e) {
        if (cartItems.isEmpty()) {
            showAlert("Empty Cart", "Your cart is already empty.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Clear Cart");
        confirmAlert.setHeaderText("Clear Shopping Cart");
        confirmAlert.setContentText("Are you sure you want to clear all items from your cart?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartItems.clear();
            selectedCartItem = null;
            currentSelectedCartButton = null;
            updateCartDisplay();
            updateTotal();
            updateButtonVisibility();
            
            showAlert("Cart Cleared", "All items have been removed from your cart.");
        }
    }

    private void handleCartItemSelection(ActionEvent e) {
        Button clickedButton = (Button) e.getSource();
        String medicineName = clickedButton.getText().substring(0, clickedButton.getText().lastIndexOf(" - $"));

        // If clicking the already selected button, do nothing
        if (medicineName.equals(selectedCartItem)) {
            return;
        }

        // Remove selection from all cart buttons
        cartItemsGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                ((Button) node).pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, false);
            }
        });

        // Set selected state for clicked button
        clickedButton.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
        currentSelectedCartButton = clickedButton;
        selectedCartItem = medicineName;
        
        updateButtonVisibility();
    }

    private void updateCartDisplay() {
        cartItemsGrid.getChildren().clear();
        
        if (cartItems.isEmpty()) {
            cartEmptyLabel.setVisible(true);
            return;
        }

        cartEmptyLabel.setVisible(false);

        for (String medicine : cartItems) {
            Button cartButton = new Button(medicine + " - $" + String.format("%.2f", medicinePrices.get(medicine)));
            cartButton.getStyleClass().add("cart-item-button");
            cartButton.setPrefWidth(230);
            cartButton.setOnAction(this::handleCartItemSelection);

            // Set initial selected state if this is the currently selected item
            if (medicine.equals(selectedCartItem)) {
                cartButton.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, true);
                currentSelectedCartButton = cartButton;
            }

            cartItemsGrid.getChildren().add(cartButton);
        }
    }

    private void updateTotal() {
        double total = 0.0;
        for (String medicine : cartItems) {
            total += medicinePrices.get(medicine);
        }
        totalLabel.setText("$" + String.format("%.2f", total));
    }

    private void updateButtonVisibility() {
        boolean hasCartItems = !cartItems.isEmpty();
        boolean hasSelectedItem = selectedCartItem != null;
        
        // Enable/disable buttons based on cart state
        clearCartButton.setDisable(!hasCartItems);
        removeItemButton.setDisable(!hasSelectedItem);
        
        // Update button opacity for visual feedback
        if (hasCartItems) {
            clearCartButton.setOpacity(1.0);
        } else {
            clearCartButton.setOpacity(0.6);
        }
        
        if (hasSelectedItem) {
            removeItemButton.setOpacity(1.0);
        } else {
            removeItemButton.setOpacity(0.6);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Navigation methods
    @FXML
    private void handleDashboard(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/dashboard.fxml", e);
    }

    @FXML
    private void handleManageSchedule(ActionEvent e) {
        navigate("/com/momentum/dosein/fxml/manage_schedule.fxml", e);
    }

    @FXML
    private void handleCostEstimator(ActionEvent e) { /* noop */ }

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