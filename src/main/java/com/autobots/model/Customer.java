package com.autobots.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class Customer {
    //Needed to use StringProperty instead of String
    private final StringProperty name;
    private final StringProperty phoneNumber;
    private final StringProperty address;

    //Default constructor
    public Customer(){
        this.name = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.address =  new SimpleStringProperty("");
    }

    public Customer(String name, String phoneNumber, String address){
        this.name = new SimpleStringProperty(name);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.address = new SimpleStringProperty(address);
    }

    public StringProperty nameProperty(){
        return name;
    }

    public StringProperty phoneProperty(){
        return phoneNumber;
    }

    public StringProperty addressProperty(){
        return address;
    }
}
