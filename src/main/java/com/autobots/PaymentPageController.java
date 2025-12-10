package com.autobots;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class PaymentPageController {

    // --- UI Elements ---
    @FXML private ListView<String> orderSummaryList;
    @FXML private Label subtotalLabel, taxLabel, tipAmountLabel, finalTotalLabel;
    @FXML private TextField customTipInput;

    //Delivery Fields
    @FXML private RadioButton deliveryRadio;
    @FXML private VBox addressBox;
    @FXML private TextField streetInput, cityInput, zipInput;

    //Payment Fields
    @FXML private RadioButton cardRadio;
    @FXML private VBox cardBox;
    @FXML private ComboBox<String> savedCardsCombo;
    @FXML private TextField cardNumberInput, expInput, cvvInput;

    //Data
    private Cart cart;
    private double tipAmount = 0.0;
    private String customerPhone;

    // --- Setup ---
    public void setOrderData(Cart cart, String phone){
        this.cart = cart;
        this.customerPhone = phone;

        for(MenuItem item : cart.getItems()){
            orderSummaryList.getItems().add(item.getName() + " - $" + item.getTotalPrice());
        }

        updateTotals();
        loadSavedCards();
    }

    private void loadSavedCards() {
        DatabaseManager db = new DatabaseManager();
        
        for(PaymentCard card : db.getPaymentMethods(customerPhone)){
            savedCardsCombo.getItems().add(card.getMaskedNumber());
        }
    }

    private void updateTotals(){
        if (cart == null) return;

        double subtotal = cart.getSubtotal();
        double tax = cart.getTax();
        double total = subtotal + tax + tipAmount;

        subtotalLabel.setText(String.format("$%.2f", subtotal));
        taxLabel.setText(String.format("$%.2f", tax));
        tipAmountLabel.setText(String.format("$%.2f", tipAmount));
        finalTotalLabel.setText(String.format("$%.2f", total));
    }

    //--- Actions 
    @FXML private void addTip10(){setTipPercent(0.10);}
    @FXML private void addTip15(){setTipPercent(0.15);}
    @FXML private void addTip20(){setTipPercent(0.20);}

    private void setTipPercent(double percent){
        if(cart != null){
            this.tipAmount = cart.getSubtotal() * percent;
            updateTotals();
            customTipInput.clear();
        }
    }

    @FXML
    private void onCustomTip(){
        try{
            this.tipAmount = Double.parseDouble(customTipInput.getText());
        } catch(NumberFormatException e){
            this.tipAmount = 0.0;
        }
        updateTotals();
    }

    @FXML 
    private void toggleDeliveryFields(){
        boolean isDelivery = deliveryRadio.isSelected();
        addressBox.setVisible(isDelivery);
        addressBox.setManaged(isDelivery);
    }

    @FXML
    private void togglePaymentFields(){
        boolean isCard = cardRadio.isSelected();
        cardBox.setVisible(isCard);
        cardBox.setManaged(isCard);
    }

    @FXML
    private void onSavedCaredSelected(){

    }

    @FXML 
    private void onPlaceOrder(){
        //Validation
        if(deliveryRadio.isSelected() && streetInput.getText().isEmpty()){
            showAlert("Error", "Please enter a delivery address");
            return;
        }

        //This will save to database but it needs the createFullOrder within the Database manager
        System.out.println("Placing order... Total: " + finalTotalLabel.getText());

        //Navigate to the confirmation page
        showAlert("Success", "Order placed successfully");
        try {
            Driver.setRoot("CustomerProfile"); //go back home
        } catch (IOException e){e.printStackTrace();}
    }

    @FXML
    private void onBack() throws IOException {
        Driver.setRoot("Transaction");
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
