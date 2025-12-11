package com.autobots;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onSignInClicked() throws Exception {
        String phone = phoneField.getText().trim();
        String pass = passwordField.getText().trim();

        if (phone.equals("1234567890") && pass.equals("password")) {
            Stage stage = (Stage) phoneField.getScene().getWindow();
            Navigation.switchTo(stage, "Menu");
        } else {
            errorLabel.setVisible(true);
            errorLabel.setText("Invalid phone number or password.");
        }
    }

    @FXML
    private void onGoToCreateClicked() throws Exception {
        Stage stage = (Stage) phoneField.getScene().getWindow();
        Navigation.switchTo(stage, "CreateAccount");
    }
}
