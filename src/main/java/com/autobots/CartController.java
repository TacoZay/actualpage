package com.autobots;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CartController {

    @FXML private TableView<MenuItem> cartTable;
    @FXML private TableColumn<MenuItem,String> nameColumn;
    @FXML private TableColumn<MenuItem,Integer> qtyColumn;
    @FXML private TableColumn<MenuItem,Void> editColumn;
    @FXML private TableColumn<MenuItem,Void> removeColumn;
    @FXML private TableColumn<MenuItem,Double> priceColumn;

    private Cart cart = new Cart();

    @FXML
    public void initialize() {
        // connect model properties to table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        // add Edit + Remove buttons
        addEditButton();
        addRemoveButton();

        cartTable.setItems(cart.getItems());
    }

    private void addEditButton() {
        editColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Edit");

            {
                btn.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());

                    // Save the pizza the user wants to edit
                    PizzaState.setEditingItem(item);

                    // Redirect to your teammate’s customization screen
                    Stage stage = (Stage) btn.getScene().getWindow();
                    try {
                        Navigation.switchTo(stage, "CustomizePizza");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void addRemoveButton() {
        removeColumn.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Remove");

            {
                btn.setOnAction(e -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    cart.removeItem(item);
                });
            }

            @Override
            protected void updateItem(Void v, boolean empty) {
                super.updateItem(v, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    @FXML
    private void onAddMoreClicked() throws Exception {
        // Go back to your teammate’s Menu page
        Stage stage = (Stage) cartTable.getScene().getWindow();
        Navigation.switchTo(stage, "Menu");
    }

    @FXML
    private void onCheckoutClicked() throws Exception {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        Navigation.switchTo(stage, "Checkout");
    }

    @FXML
    private void onBackClicked() throws Exception {
        Stage stage = (Stage) cartTable.getScene().getWindow();
        Navigation.switchTo(stage, "Menu");
    }
}

