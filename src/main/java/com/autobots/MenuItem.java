package com.autobots;

public class MenuItem {
    private String name;
    private double price;
    private int quantity;

    // --- Constructor ---
    public MenuItem(String name, double price, int quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    //--- Getters ---
    public String getName(){return name;}
    public double getPrice() {return price;}
    public int getQuantity(){return quantity;}
    public double getTotalPrice(){return price * quantity;}

}
