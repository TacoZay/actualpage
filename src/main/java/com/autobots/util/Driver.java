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
    private static LetterboxPane rootPane; 
    public static Customer currentUser = null;

    private static final double BASE_WIDTH = 1000.0;
    private static final double BASE_HEIGHT = 700.0;

    @Override
    public void start(Stage stage) throws IOException {
       Parent initialContent = loadFXML("LandingPage");

       rootPane = new LetterboxPane(initialContent);
       
       scene = new Scene(rootPane, 1000, 700);

       stage.setTitle("Mom and Pop Pizza");
       stage.setScene(scene);
       stage.show();
      
    }

    public static void setRoot(String fxml) throws IOException {
        //When chaning pages, we only replace the content inside the wrapper
        Parent newContent = loadFXML(fxml);
        scene.setRoot(new LetterboxPane(newContent));
    }

   
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource( "/com/autobots/"+fxml+".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

}