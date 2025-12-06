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


public class CustomerProfileController {

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


    private Customer currentCustomer;

    @FXML
    public void initialize(){                                                                               //why does initialize() have to done manually? Is this considered the initializing of values
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
