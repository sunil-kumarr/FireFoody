package com.example.rapidfood.Models;

public class UserModel {

    private UserAddressModal address_first;
    private UserProfileModel user_profile;
    private String balance;
    private boolean subscribed;


    public UserProfileModel getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(UserProfileModel pUser_profile) {
        user_profile = pUser_profile;
    }

    public UserAddressModal getAddress_first() {
        return address_first;
    }

    public void setAddress_first(UserAddressModal pAddress_first) {
        address_first = pAddress_first;
    }

    public String getBalance() {
        return balance;
    }
    public boolean isSubscribed() {
        return subscribed;
    }
}
