package com.autobots;

import java.io.IOException;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TransactionController {
    
    @FXML private TableView<MenuItem> cartTable;
    @FXML private TableColumn<MenuItem, String> itemCol;
    @FXML private TableColumn<MenuItem, Number> priceCol;

    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;
    @FXML private ComboBox<String> paymentCombo;
    @FXML private ComboBox<String> pizzaCombo;
    @FXML private TextField tipInput;

    private Cart cart = new Cart();
    private String customerPhone;
    private double currentPizzaPrice = 0.0;

    public void setCustomer(String phone){
        this.customerPhone = phone;
        loadPaymentMethods();
    }

    @FXML
    public void initialize(){
        itemCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getName()));
        priceCol.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getTotalPrice()));

        cartTable.setItems(cart.getItems());
    }

    // --- Menu Actions ---
    @FXML void addCheese(){ addToCart("Cheese Pizza", 10.00);}
    @FXML void addPep(){ addToCart("Pepperoni Pizza", 12.00);}
    @FXML void addVeg(){ addToCart("Veggie Pizza", 15.00);}
    @FXML void addSoda(){ addToCart("Soda Pizza", 2.00);}

    private void addToCart(String name, double price){
        cart.addItem(name, price);
        updateTotals();
    }

    private void updateTotals(){
        subtotalLabel.setText(String.format("$%.2f", cart.getSubtotal()));
        taxLabel.setText(String.format("$%.2f", cart.getTax()));
        totalLabel.setText(String.format("$%.2f", cart.getTotal()));

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

        PaymentPageController controller = loader.getController();
        controller.setOrderData(this.cart, this.customerPhone);

        Stage stage = (Stage) cartTable.getScene().getWindow();
        stage.setScene(new Scene(root));
    }


    //--- Navigation Controls

    @FXML 
    private void onMenu(){
        try {
            Driver.setRoot("MenuOrderPage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCart(){
        System.out.println("Cart clicked");
    }

    @FXML
    private void onBack(){
        try{
            Driver.setRoot("CustomerProfile");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
