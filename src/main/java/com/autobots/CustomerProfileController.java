package com.autobots;

import application.model.Customer;
import application.model.Order;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class CustomerProfileController {


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

    
    //for now this is a placeholder. This will need the Order.java model for this to be used

    //data fields
    private Customer currentCustomer;

    @FXML
    public void initialize(){
        pizzaTypeCol.setCellValueFactory(cellData -> cellData.getValue().pizzaTypeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tipCol.setCellValueFactory(cellData -> cellData.getValue().tipProperty());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        //This is the placeholder data (needs to be actual data later)
        loadCustomerData(new Customer("Guest Name", "555-123-4567", "ABC Street"));
    }

    void loadCustomerData(Customer customer){
        this.currentCustomer = customer;
        nameLabel.textProperty().bind(customer.nameProperty());
        phoneLabel.textProperty().bind(customer.phoneProperty());
        addressLabel.textProperty().bind(customer.addressProperty());
    }

}
