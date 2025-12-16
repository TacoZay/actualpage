package com.autobots.controller;

import java.io.IOException;

import com.autobots.model.Customer;
import com.autobots.util.DatabaseManager;
import com.autobots.util.Driver;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CreateAccountController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    public void initialize() {
        // --- 1. Phone Number Auto-Formatting ---
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Strip out everything except numbers
            String digits = newValue.replaceAll("[^\\d]", "");
            
            // Limit to 10 digits
            if (digits.length() > 10) {
                digits = digits.substring(0, 10);
            }

            // Build the format: (XXX) XXX - XXXX
            StringBuilder formatted = new StringBuilder();
            if (digits.length() > 0) {
                formatted.append("(");
                int len = digits.length();
                
                if (len <= 3) {
                    formatted.append(digits);
                } else {
                    formatted.append(digits.substring(0, 3)).append(") ");
                    if (len <= 6) {
                        formatted.append(digits.substring(3));
                    } else {
                        formatted.append(digits.substring(3, 6)).append(" - ").append(digits.substring(6));
                    }
                }
            }

            // Update field only if text is different (prevents infinite loops)
            if (!formatted.toString().equals(newValue)) {
                phoneField.setText(formatted.toString());
                phoneField.positionCaret(formatted.length()); // Move cursor to end
            }
        });
    }

    @FXML
    private void onCreateClicked() {
        String name = nameField.getText().trim();
        
        // --- 2. Clean Phone Number for Database ---
        // Convert "(578) 311 - 4000" back to "5783114000"
        String rawPhone = phoneField.getText().replaceAll("[^0-9]", "");
        
        String pass = passwordField.getText().trim();

        if (name.isEmpty() || rawPhone.isEmpty() || pass.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("All fields are required.");
            return;
        }

        if (rawPhone.length() != 10) {
            errorLabel.setVisible(true);
            errorLabel.setText("Phone number must be 10 digits.");
            return;
        }

        // --- 3. Save & Login ---
        DatabaseManager db = new DatabaseManager();
        
        // Note: We pass 'rawPhone' (digits only) to the database
        boolean success = db.createCustomer(name, rawPhone, pass);

        if (success) {
            System.out.println("Account Created! Logging in...");
            
            // FIX: Manually set the user so the app knows we aren't a guest anymore
            Driver.currentUser = new Customer(name, rawPhone, "No Address Set"); 
            
            try {
                Driver.setRoot("LandingPage");
            } catch (IOException e) { e.printStackTrace(); }
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Error: Phone number already exists.");
        }
    }

    @FXML
    private void onBackClicked() throws IOException {
        Driver.setRoot("Login");
    }
}