module com.autobots {
    //JavaFX Requirements
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    //Dependencies
    requires java.sql;
    requires mysql.connector.j;
    requires com.gluonhq.charm.glisten;
    requires com.gluonhq.attach.util;
    
    //Opens the Controller package for FXML
    //fix: opens the new contorller package for FXML access
    opens com.autobots.controller to javafx.fxml;

    //Opens the Model package for Data binding
    //fix: opens the model package to be used in TableView/ListView(FX properties)
    opens com.autobots.model to javafx.base;

    //Export the Utility package
    //the package containing the application's entry point
    exports com.autobots.util;
}
