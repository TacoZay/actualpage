package com.autobots;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PaymentPageController {

    // --- Header ---
    @FXML private Label headerNameLabel;

    // --- Left Side Summary
    @FXML private ListView<String> orderSummaryList;
    @FXML private Label subtotalLabel, taxLabel, tipAmountLabel, finalTotalLabel;
    @FXML private TextField customTipInput;

    //--- Right Side Forms
    //Delivery Fields
    @FXML private RadioButton pickupRadio, deliveryRadio;
    @FXML private VBox storeInfoBox;
    @FXML private VBox deliveryFormBox;

    @FXML private TextField streetInput, street2Input, cityInput, stateInput, zipInput;

    //Payment Fields
    @FXML private RadioButton cardRadio;
    @FXML private VBox cardBox;
    @FXML private VBox billingAddressBox;
    @FXML private CheckBox sameAddressCheck;
    @FXML private TextField billingStreetInput, billingCityInput, billingZipInput;

    //Card Details
    @FXML private ComboBox<String> savedCardsCombo;
    @FXML private TextField cardNumberInput, expInput, cvvInput;
    @FXML private TextField signatureInput;

    //Data
    private Cart cart;
    private double tipAmount = 0.0;
    private String customerPhone;

    // --- Initialization ---
    public void setOrderData(Cart cart, String phone){
        this.cart = cart;
        this.customerPhone = phone;

        
        refreshOrderList();
        updateTotals();
        loadSavedCards();

        //Initial State: carryout is selected
        toggleOrderType();
    }

    private void refreshOrderList(){
        orderSummaryList.getItems().clear();
        if(cart != null){
            for(MenuItem item : cart.getItems()){
                //We will add the item index to the string so we know which one to delete later
                orderSummaryList.getItems().add(item.getName() + " -$" + String.format("$%.2f", item.getTotalPrice()));
            }
        }
    }


    private void loadSavedCards() {
        DatabaseManager db = new DatabaseManager();
        
        for(PaymentCard card : db.getPaymentMethods(customerPhone)){
            savedCardsCombo.getItems().add(card.getMaskedNumber());
        }
    }

    //--- Order Summary Logic ---
    @FXML 
    private void onRemoveItem(){
        int selectedIndex = orderSummaryList.getSelectionModel().getSelectedIndex();
        if(selectedIndex >= 0){
            cart.getItems().remove(selectedIndex);

            //Refresh UI
            refreshOrderList();
            updateTotals();
        } else {
            showAlert("Selection Error", "Please select an item to remove.");
        }
    }

    private void updateTotals(){
        if (cart == null) return;

        double subtotal = cart.getSubtotal();
        double tax = cart.getTax();
        double total = subtotal + tax + tipAmount;

        subtotalLabel.setText(String.format("$%.2f", subtotal));
        taxLabel.setText(String.format("$%.2f", tax));

        if(tipAmountLabel != null){
            tipAmountLabel.setText(String.format("Tip Added: $%.2f", total));
        }
    
        finalTotalLabel.setText(String.format("$%.2f", total));
    }

    //--- Tip Logic ---
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

    //--- Toggles ---
    @FXML 
    private void toggleOrderType(){
        boolean isDelivery = deliveryRadio.isSelected();

        //Show Delivery Form only if delivery is selected
        deliveryFormBox.setVisible(isDelivery);
        deliveryFormBox.setManaged(!isDelivery);

        //Show Store Info only if pickup is selected
        storeInfoBox.setVisible(!isDelivery);
        storeInfoBox.setManaged(!isDelivery);

        //If we switch to pickup, we can uncheck "Same as delivery" logic
        if(!isDelivery){
            sameAddressCheck.setSelected(false);
            toggleBillingAddress();
        }

    }

    @FXML 
    private void togglePaymentFields(){ 
        boolean isCard = cardRadio.isSelected();
        cardBox.setVisible(isCard);
        cardBox.setManaged(isCard);
    }

    @FXML
    private void toggleBillingAddress(){
        boolean isSame = sameAddressCheck.isSelected();
        billingAddressBox.setVisible(!isSame);
        billingAddressBox.setManaged(!isSame);

        if(isSame && deliveryRadio.isSelected()){
            billingStreetInput.setText(streetInput.getText());
            billingCityInput.setText(cityInput.getText());
            billingZipInput.setText(zipInput.getText());
        } else {
            billingStreetInput.clear();
        }
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
