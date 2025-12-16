module com.autobots {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires com.gluonhq.charm.glisten;
    

    //fix: opens the new contorller package for FXML access
    opens com.autobots.controller to javafx.fxml;

    //fix: opens the model package to be used in TableView/ListView(FX properties)
    opens com.autobots.model to javafx.base;

    exports com.autobots;
}
