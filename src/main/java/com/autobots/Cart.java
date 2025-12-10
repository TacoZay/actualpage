package com.autobots;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
    //the Observablelist will allow the UI Table to automatically update when items are added
    private ObservableList<MenuItem> items = FXCollections.observableArrayList();

    public ObservableList<MenuItem> getItems(){return items;}

    public void addItem(String name, double price){
        items.add(new MenuItem(name, price, 1));
    }

    public void clear(){
        items.clear();
    }

    public double getSubtotal(){
        return items.stream().mapToDouble(MenuItem::getTotalPrice).sum();
    }

    public double getTax(){
        return getSubtotal() * 0.07;
    }

    public double getTotal(){
        return getSubtotal() + getTax();
    }
}
