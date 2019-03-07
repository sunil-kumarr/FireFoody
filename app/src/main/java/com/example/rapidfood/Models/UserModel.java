package com.example.rapidfood.Models;

public class UserModel {
    private String username;
    private String mobile;
    private String address;

    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String pMobile) {
        mobile = pMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String pAddress) {
        address = pAddress;
    }

    public UserModel(String pUsername, String pMobile, String pAddress) {
        username = pUsername;
        mobile = pMobile;
        address = pAddress;
    }
}
