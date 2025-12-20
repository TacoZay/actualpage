package com.autobots.util;

import java.io.IOException;

import com.autobots.model.Customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class Driver extends Application {

    private static Scene scene;

    public static Customer currentUser = null;

    @Override
    public void start(Stage stage) throws IOException {

        final double BASE_WIDTH = 1000.0;
        final double BASE_HEIGHT = 700.0;

        scene = new Scene(loadFXML("Login"),BASE_WIDTH, BASE_HEIGHT);
        
        Parent root = scene.getRoot();

        scene.widthProperty().addListener((obs, oldVal, newVal) ->{
            
            double scale = newVal.doubleValue() / BASE_WIDTH;
            root.setScaleX(scale);
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) ->{
            
            double scale = newVal.doubleValue() / BASE_HEIGHT;
            root.setScaleX(scale);
        });

        stage.setTitle("Mom and Pop Pizza");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource( "/com/autobots/"+fxml+".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}