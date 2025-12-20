package com.autobots.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

public class LetterboxPane extends StackPane {
    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();

    public LetterboxPane(Node content){
        this.content.set(content);

        //Create a group to hold the content
        Group group = new Group(content);

        double contentWidth = 1000.0;
        double contentHeight = 700.0;

        //Add a scale to transform
        Scale scale = new Scale();
        group.getTransforms().add(scale);

        //These listeners will resize content when THIS pane resizes
        widthProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, contentWidth, contentHeight));
        heightProperty().addListener((obs, oldVal, newVal) -> updateScale(scale, contentWidth, contentHeight));

         getChildren().add(group);
    }

    private void updateScale(Scale scale, double contentWidth, double contentHeight){
        double width = contentWidth;
        double height = contentHeight;

        if (width == 0 || height == 0){
           return; 
        } 

        double scaleX = width / contentWidth;
        double scaleY = height / contentHeight;

        //use this to stretch
        scale.setX(scaleX);
        scale.setY(scaleY);

       
    }
}
