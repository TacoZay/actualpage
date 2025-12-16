package com.autobots.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigation {

    /**
     * Switches the current window (Stage) to the given FXML page name.
     * @param stage The window to display the new page in
     * @param fxmlName The name of the FXML file WITHOUT extension
     * @throws Exception if file not found or load fails
     */
    public static void switchTo(Stage stage, String fxmlName) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                Navigation.class.getResource("/com/autobots/" + fxmlName + ".fxml")
        );

        if (loader.getLocation() == null) {
            throw new RuntimeException("FXML not found: " + fxmlName);
        }

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}
