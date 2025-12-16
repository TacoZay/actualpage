package com.autobots.controller;

import java.io.IOException;

import com.autobots.model.Customer;
import com.autobots.util.DatabaseManager;
import com.autobots.util.Driver;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    Driver Driver = new Driver();

    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onSignInClicked() {
        String phone = phoneField.getText().trim();
        String pass = passwordField.getText().trim();

        DatabaseManager db = new DatabaseManager();
        Customer customer = db.validateUser(phone, pass);

        if (customer != null) {
            // LOGIN SUCCESS
            Driver.currentUser = customer; // Save to global state
            navigateToLanding();
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid phone number or password.");
        }
    }

    @FXML
    private void onGuestClicked() {
        // GUEST MODE
        Driver.currentUser = null; // Ensure it is null
        navigateToLanding();
    }

    @FXML
    private void onGoToCreateClicked() throws IOException {
        Driver.setRoot("CreateAccount");
    }

    private void navigateToLanding() {
        try {
            Driver.setRoot("LandingPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
