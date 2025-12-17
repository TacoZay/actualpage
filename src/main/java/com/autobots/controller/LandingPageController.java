package com.autobots.controller;

import java.io.IOException;

import com.autobots.util.Driver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class LandingPageController {
    
    Driver Driver = new Driver();
    
    //added the FXML field for the button
    @FXML private Button customerNameButton;
    
    @FXML 
    private void onMenu() throws IOException {
        Driver.setRoot("MenuOrderPage");
    }

    @FXML
    private void onPickup() throws IOException {

        System.out.println("Pickup Selected");
        Driver.setRoot("MenuOrderPage");
    }

    @FXML
    private void onDelivery() throws IOException {
        System.out.println("Delivery Selected");
        Driver.setRoot("MenuOrderPage");
    }

    @FXML
    private void onProfile() throws IOException {
        if (Driver.currentUser != null) {
            Driver.setRoot("CustomerProfile");
        } else {
            System.out.println("Guest cannot access profile.");
            // Optional: Redirect to Login or show Alert
            Driver.setRoot("Login");
        }
    }

    @FXML 
    public void initialize(){
        if(Driver.currentUser != null){
            /* if a user is signed in, it will bind the button text to the customer's name
            It will automatically update if the name changes */
            customerNameButton.textProperty().bind(Driver.currentUser.nameProperty());
        } else {
            //if running as guest, the text will say "Guest"
            customerNameButton.setText("Button");
        }
    }
}
