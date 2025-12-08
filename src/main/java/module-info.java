module com.autobots {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    //requires com.mysql.cj;
    

    opens com.autobots to javafx.fxml;
    exports com.autobots;
}
