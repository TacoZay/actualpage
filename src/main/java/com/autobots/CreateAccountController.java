package com.autobots;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class CreateAccountController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onCreateClicked() throws Exception {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setText("All fields are required.");
            return;
        }

        if (!phone.matches("\\d{10}")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Phone number must be 10 digits.");
            return;
        }

        if (phone.equals("1234567890")) {
            errorLabel.setVisible(true);
            errorLabel.setText("Phone number already registered.");
            return;
        }

        Stage stage = (Stage) nameField.getScene().getWindow();
        Navigation.switchTo(stage, "Menu");
    }

    @FXML
    private void onBackClicked() throws Exception {
        Stage stage = (Stage) nameField.getScene().getWindow();
        Navigation.switchTo(stage, "Login");
    }
}
