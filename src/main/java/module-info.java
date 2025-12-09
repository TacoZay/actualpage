module com.autobots {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    

    opens com.autobots to javafx.fxml;
    exports com.autobots;
}
