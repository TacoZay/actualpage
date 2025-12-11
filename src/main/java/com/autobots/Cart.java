package com.autobots;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {

    private ObservableList<MenuItem> items = FXCollections.observableArrayList();

    public ObservableList<MenuItem> getItems(){ return items; }

    public void addItem(MenuItem item){
        items.add(item);
    }

    public void removeItem(MenuItem item){
        items.remove(item);
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
