package com.autobots;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class MenuOrderPageController {
    @FXML private ListView<String> miniCartList;
    @FXML private Label subtotalLabel;

    private Cart cart = new Cart();

    @FXML
    public void initialize(){
        updateMiniCart();
    }

    // --- Menu Actions ---
    @FXML void addCheese() { addToCart("Cheese Pizza", 10.00);}
    @FXML void addPep() { addToCart("Pepperoni Pizza", 12.00);}
    @FXML void addVeg() { addToCart("Veggie Pizza", 15.00);}
    @FXML void addSoda() { addToCart("Soda", 2.00);}

    private void addToCart(String name, double price){
        cart.addItem(name, price);
        updateMiniCart();
    }

    private void updateMiniCart(){
        miniCartList.getItems().clear();
        for(MenuItem item : cart.getItems()){
            miniCartList.getItems().add(item.getName() + " - $" + item.getPrice());
        }
        subtotalLabel.setText(String.format("$%.2f", cart.getSubtotal()));
    }

    //--- Navigation ---
    @FXML
    private void onViewOrder() throws IOException {
        Driver.setRoot("Transaction");
    }

    @FXML
    private void onHome() throws IOException {
        Driver.setRoot("LandingPage");
    }

    @FXML 
    private void onLogout() throws IOException {
        Driver.setRoot("LandingPage");
    }
}
