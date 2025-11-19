module com.autobots {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.autobots to javafx.fxml;
    exports com.autobots;
}
