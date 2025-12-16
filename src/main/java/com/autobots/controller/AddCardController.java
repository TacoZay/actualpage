package com.autobots.controller;

import com.autobots.util.DatabaseManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddCardController {
    @FXML private TextField holderInput;
    @FXML private TextField numberInput;
    @FXML private TextField expInput;
    @FXML private TextField cvvInput;

    private String customerPhone;
    private CustomerProfileController mainController;

    //this is the logic to receive the date from the main window
    public void setCustomerInfo(String phone, CustomerProfileController controller){
        this.customerPhone = phone;
        this.mainController = controller;
    }

    @FXML
    private void onSave(){
        String holder = holderInput.getText();
        String number = numberInput.getText();
        String exp = expInput.getText();
        String cvv = cvvInput.getText();

        //1. basic validation
        if(holder.isEmpty() || number.isEmpty() || exp.isEmpty() || cvv.isEmpty()){
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        //2. Saves to database
        DatabaseManager db = new DatabaseManager();
        String result = db.addPaymentMethod(customerPhone, number, holder, exp, cvv);

        if(result.equals("Success")){
            mainController.loadPaymentMethods();
            closeWindow();
        } else {
            showAlert("Error", result);
        }
    }

    @FXML
    private void onCancel(){
        closeWindow();
    }


    //--- Helper methods 
    private void closeWindow(){
        Stage stage = (Stage) holderInput.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    } 
}
