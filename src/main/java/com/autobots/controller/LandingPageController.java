package com.autobots.controller;

import java.io.IOException;

import com.autobots.util.Driver;

import javafx.fxml.FXML;

public class LandingPageController {
    
    Driver Driver = new Driver();
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
}
