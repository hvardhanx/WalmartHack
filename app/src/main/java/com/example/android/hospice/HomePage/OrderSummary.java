package com.example.android.hospice.HomePage;

/**
 * Created by samsrutidash on 7/26/2016.
 */
public class OrderSummary {
    String name;
    String email;
    String address;
    String phone;

    OrderSummary(String name, String email, String address, String phone){
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
    @Override
    public String toString(){
        return name+"\n"+phone+"\n"+address+"\n"+email;
    }
}
