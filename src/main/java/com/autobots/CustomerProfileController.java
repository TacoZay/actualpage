package com.autobots;

import application.model.Customer;
import appliation.model.Order;
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
    //for now this is a placeholder. This will need to 
}
