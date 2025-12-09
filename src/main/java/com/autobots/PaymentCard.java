package com.autobots;

public class PaymentCard {
    private String number;
    private String holder;
    private String expiration;
    private String cvv;

    public PaymentCard(String number, String holder, String expiration, String cvv){
        this.number = number;
        this.holder = holder; 
        this.expiration = expiration;
        this.cvv = cvv;
    }

    public String getNumber(){return number;}
    public String getHolder(){return holder;}
    public String getExpiration(){return expiration;}

    public String getMaskedNumber(){
        if(number != null && number.length() > 4){
            return "**** **** **** " + number.substring(number.length() - 4);
        }
        return number;
    }
    
}
