module com.autobots {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    

    opens com.autobots to javafx.fxml;
    exports com.autobots;
}
