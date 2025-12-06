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


    /* --- Edit controls */
    @FXML private TextField nameInput;
    @FXML private TextField phoneInput;
    @FXML private TextField addressInput;
    @FXML private Button editButton;


    @FXML
    private void onToggleEdit(){
        if(editButton.getText().equals("Edit")){
            nameInput.setText(currentCustomer.nameProperty().get());
            phoneInput.setText(currentCustomer.phoneProperty().get());
            addressInput.setText(currentCustomer.addressProperty().get());

            toggleVisibility(true);
            editButton.setText("Save");
        } else {

            /* --- Save Changes --- */
            currentCustomer.nameProperty().set(nameInput.getText());
            currentCustomer.phoneProperty().set(phoneInput.getText());
            currentCustomer.addressProperty().set(addressInput.getText());

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

    void loadCustomerData(Customer customer){
        this.currentCustomer = customer;
        nameLabel.textProperty().bind(Bindings.concat("Name: ", customer.nameProperty()));
        phoneLabel.textProperty().bind(Bindings.concat("Phone: ", customer.phoneProperty()));
        addressLabel.textProperty().bind(Bindings.concat("Address: ", customer.addressProperty()));
    }

}
