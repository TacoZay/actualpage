package com.autobots;

public class Customer {
    private String name;
    private String phoneNumber;
    private String address;

    public Customer(){
        this.name = "";
        this.phoneNumber = "";
        this.address = "";
    }

    public Customer(String name, String phoneNumber, String address){
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
