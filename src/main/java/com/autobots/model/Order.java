package com.autobots.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Order {
    private final StringProperty pizzaType;
    private final StringProperty date;
    private final StringProperty total;
    private final StringProperty tip;

    public Order(String pizzaType, String date, String total, String tip){
        this.pizzaType = new SimpleStringProperty(pizzaType);
        this.date = new SimpleStringProperty(date);
        this.total = new SimpleStringProperty(total);
        this.tip = new SimpleStringProperty(tip);
    }

    public StringProperty pizzaTypeProperty(){
        return pizzaType;
    }


    public StringProperty dateProperty(){
        return date;
    }

    public StringProperty totalProperty(){
        return total;
    }

    public StringProperty tipProperty(){
        return tip;
    }
}
