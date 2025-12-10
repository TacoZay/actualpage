package com.autobots;

import java.io.IOException;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerProfileController {
    
    
    private Customer currentCustomer;
    private String originalPhoneNumber;


    @FXML private AnchorPane rootPane;
    @FXML private Label profileLabel;
    @FXML private Label headerNameLabel;

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
    private ListView<PaymentCard> paymentList;

    /* --- Navigation Controls ---  */
    @FXML
    private void onMenu() throws IOException {

        try{
           Driver.setRoot("MenuOrderPage");
        }catch(IOException e){
            e.printStackTrace();
        }
        

        //needs a way to pass the phoneNum to the new controller
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
        Driver.setRoot("LandingPage");
        System.out.println("Navigate to previous page...");
    }

    @FXML
    private void onAddCard(){
       try{
        //Loads the new FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCard.fxml"));
        Parent root = loader.load();

        //Pass the data to the new controller
        AddCardController popupController = loader.getController();
        popupController.setCustomerInfo(originalPhoneNumber, this);

        //Create and show the new window
        Stage stage = new Stage();
        stage.setTitle("Add Payment Method");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

       }catch(IOException e){
        e.printStackTrace();
        System.out.println("Error: " + e.getMessage());
       }

    }

    @FXML
    private void onDeleteCard(){
        PaymentCard selected = paymentList.getSelectionModel().getSelectedItem();
        if(selected == null ){
            showAlert("Selection Error", "Please select a card to delete.");
            return;
        }

        DatabaseManager db = new DatabaseManager();
        db.deletePaymentMethod(originalPhoneNumber, selected.getNumber());
        loadPaymentMethods();
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
            boolean success = db.updateCustomer(currentCustomer, originalPhoneNumber);

            if(success){
                originalPhoneNumber = currentCustomer.phoneProperty().get();
            }

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
    

    @FXML
    public void initialize(){      
        profileLabel.styleProperty().bind(Bindings.concat("-fx-font-size: ", rootPane.widthProperty().divide(35), "px;"));                                                                        //why does initialize() have to done manually? Is this considered the initializing of values
        pizzaTypeCol.setCellValueFactory(cellData -> cellData.getValue().pizzaTypeProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tipCol.setCellValueFactory(cellData -> cellData.getValue().tipProperty());
        totalCol.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        // --- Database loading
        DatabaseManager db = new DatabaseManager();
        Customer dbCustomer = db.getCustomer("578-311-4000");

        if (dbCustomer != null){
            loadCustomerData(dbCustomer);
        } else { 
            System.out.println("Customer is not found!");
        }

        // --- Custom List Cell Factory
        //This will tell the list how to display a "PaymentCard Object"
        paymentList.setCellFactory(param -> new javafx.scene.control.ListCell<PaymentCard>(){
            @Override
            protected void updateItem(PaymentCard card, boolean empty){
                super.updateItem(card, empty);

                if(empty || card == null){
                    setText(null);
                    setGraphic(null);
                } else{
                    //Creates a container for the cell
                    javafx.scene.layout.VBox container = new javafx.scene.layout.VBox(5);

                    //The Masked number
                    Label numberLbl = new Label(card.getMaskedNumber());
                    numberLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    //The Subtext (Name | Exp)
                    Label detailsLbl = new Label(card.getHolder() + " | Exp: " + card.getExpiration());
                    detailsLbl.setStyle("-fx-text-fill: #666666; -fx-font-size: 12px;");

                    //add them to the container
                    container.getChildren().addAll(numberLbl, detailsLbl);
                    setGraphic(container);
                }
            }}
            
    
        );
    }


    // --- Helper Methods ---
    void loadCustomerData(Customer customer){
        this.currentCustomer = customer;

        this.originalPhoneNumber = customer.phoneProperty().get();

        nameLabel.textProperty().bind(customer.nameProperty());
        phoneLabel.textProperty().bind(customer.phoneProperty());
        addressLabel.textProperty().bind(customer.addressProperty());

        if(headerNameLabel != null){
            headerNameLabel.textProperty().bind(customer.nameProperty());
        }
        
        loadPaymentMethods();
    }


    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadPaymentMethods(){
        DatabaseManager db = new DatabaseManager();
        List<PaymentCard> cards = db.getPaymentMethods(originalPhoneNumber);
        paymentList.getItems().setAll(cards);
    }
}
