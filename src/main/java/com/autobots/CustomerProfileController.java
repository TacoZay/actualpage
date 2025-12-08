package com.autobots;

import com.autobots.Customer;
import com.autobots.Order;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.binding.Bindings;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import javafx.scene.control.Alert;

public class CustomerProfileController {

    @FXML private AnchorPane rootPane;
    @FXML private Label profileLabel;

    //Contact Info
    @FXML
    private Label nameLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label addressLabel;

    //Order History
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> pizzaTypeCol;
    @FXML
    private TableColumn<Order, String> dateCol;
    @FXML
    private TableColumn<Order, String> tipCol;
    @FXML
    private TableColumn<Order, String> totalCol;

    @FXML
    private ListView<String> paymentList;

    /* --- Navigation Controls ---  */
    @FXML
    private void onMenu(){
        System.out.println("Navigate to Menu...");
        //App.setRoot("menu");
    }

    @FXML
    private void onCart(){
        System.out.println("Navigate to Cart...");
        //App.setRoot("cart");
    }
    @FXML
    private void onLogout() {
        //Clear user session if needed
        // App.setRoot("primary");
    }

    @FXML
    private void onBack() throws IOException {
        Driver.setRoot("primary");
        System.out.println("Navigate to previous page...");
    }


    /* --- Edit controls */
    @FXML private TextField nameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField addressInput;
    @FXML private Button editButton;


    @FXML
    private void onToggleEdit(){
        if(editButton.getText().equals("Edit")){

            // This will switch to edit mode
            nameInput.setText(currentCustomer.nameProperty().get());
            phoneInput.setText(currentCustomer.phoneProperty().get());
            addressInput.setText(currentCustomer.addressProperty().get());

            //Clear any old error styles
            nameInput.setStyle(null);
            phoneInput.setStyle(null);
            addressInput.setStyle(null);

            toggleVisibility(true);
            editButton.setText("Save");
        } else {
            // --- Validation Check ---
            String newName = nameInput.getText().trim();
            String newPhone = phoneInput.getText().trim();
            String newAddress = addressInput.getText().trim();

            //Check for empty fields
            if(newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()){
                showAlert("Input Error", "Please fil in all fields.");
                return;         //Stop and don't save
            }

            //Checks the address format(must have a comma for database manager logic)
            if(!newAddress.contains(",")){
                showAlert("Invalid Address Format", "Address must be: Street, City, State Zip");
                addressInput.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                return;
            }
            /* --- Save Changes --- */
            //This will update the local java object(UI will be updated)
            currentCustomer.nameProperty().set(nameInput.getText());
            currentCustomer.phoneProperty().set(phoneInput.getText());
            currentCustomer.addressProperty().set(addressInput.getText());


            //Send to Database
            DatabaseManager db = new DatabaseManager();
            db.updateCustomer(currentCustomer);


            toggleVisibility(false);
            editButton.setText("Edit");
        }
    }

    private void toggleVisibility(boolean isEditing){
        //Toggle Inputs
        nameInput.setVisible(isEditing);
        phoneInput.setVisible(isEditing);
        addressInput.setVisible(isEditing);

        //Toggle Labels
        nameLabel.setVisible(!isEditing);
        phoneLabel.setVisible(!isEditing);
        addressLabel.setVisible(!isEditing);
    }
    private Customer currentCustomer;

    @FXML
    public void initialize(){      
        profileLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", rootPane.widthProperty().divide(35), "px;"));                                                                        //why does initialize() have to done manually? Is this considered the initializing of values
        pizzaTypeCol.setCellValueFactory(cellData -> cellData.getValue().pizzaTypeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tipCol.setCellValueFactory(cellData -> cellData.getValue().tipProperty());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        //This is the placeholder data (needs to be actual data later)
        Customer mockCustomer = new Customer("Xavier Terry", "578-311-4000", "4321 Street St, Marietta, GA");
        loadCustomerData(mockCustomer);
    }


    // --- Helper Methods ---
    void loadCustomerData(Customer customer){
        this.currentCustomer = customer;
        nameLabel.textProperty().bind(customer.nameProperty());
        phoneLabel.textProperty().bind(customer.phoneProperty());
        addressLabel.textProperty().bind(customer.addressProperty());
    }


    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
