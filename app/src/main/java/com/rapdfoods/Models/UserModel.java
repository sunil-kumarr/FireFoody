package com.rapdfoods.Models;

public class UserModel {

    private UserAddressModal address_first;
    private UserProfileModel user_profile;
    private String balance;
    private boolean subscribed;
    private String firebase_id;
    private String mobile;

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

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
