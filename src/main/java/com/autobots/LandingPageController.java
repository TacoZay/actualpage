package com.autobots;

import java.io.IOException;

import javafx.fxml.FXML;

public class LandingPageController {
    
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
        Driver.setRoot("CustomerProfile");
    }
}
