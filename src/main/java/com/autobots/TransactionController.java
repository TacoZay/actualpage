package com.autobots;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TransactionController {
    @FXML private ComboBox<String> pizzaCombo;
    @FXML private TextField tipInput;
    @FXML private ComboBox<String> paymentCombo;
    @FXML private Label totalLabel;

    private String customerPhone;
    private double currentPizzaPrice = 0.0;

    public void setCustomer(String phone){
        this.customerPhone = phone;
        loadPaymentMethods();
    }

    @FXML
    public void initialize(){
        pizzaCombo.getItems().addAll("Cheese ($10.00)", "Pepperoni ($12.00)", "Veggie ($15.00)");
    }

    private void loadPaymentMethods(){
        DatabaseManager db = new DatabaseManager();
        List<PaymentCard> cards = db.getPaymentMethods(customerPhone);

        for(PaymentCard card : cards){
            paymentCombo.getItems().add(card.getMaskedNumber());
        }
    }

    @FXML
    private void calculateTotal(){
        //Determine Pizza price
        String selected = pizzaCombo.getValue();
        if(selected != null){
            if(selected.contains("$10")) currentPizzaPrice = 10.00;
            else if (selected.contains("$12")) currentPizzaPrice = 12.00;
            else if(selected.contains("$15")) currentPizzaPrice = 15.00;
        }

        double tip = 0.0;
        try {
            if(!tipInput.getText().isEmpty()){
                tip = Double.parseDouble(tipInput.getText());
            }
        } catch(NumberFormatException e) {

        }

        double total = currentPizzaPrice + tip;
        totalLabel.setText(String.format("Total: $%.2f", total));
    }

    @FXML 
    private void onPlaceOrder(){
        if(pizzaCombo.getValue() == null  || paymentCombo.getValue() == null){
            showAlert("Error", "Please select a pizza type and a payment method");
            return;
        }

        double tip = 0.0;
        try{ tip = Double.parseDouble(tipInput.getText()); }catch(Exception e){}
        
        DatabaseManager db = new DatabaseManager();
        boolean success = db.createOrder(customerPhone, pizzaCombo.getValue(), currentPizzaPrice, tip);

        if(success){
            showAlert("Success", "Order Placed Successfully!");
            onCancel();
        } else {
            showAlert("Error", "Failed to place order.");
        }
    }

    @FXML
    private void onCancel(){
        try{
            Driver.setRoot("CustomerProfile");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void onCheckout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentPage.fxml"));
        Parent root = loader.load();
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
