package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class SubscribedUserModel {
    private String balance;
    @ServerTimestamp
    private Date start_date;
    private String duration;
    private String subscriptionType;
    private String trans_id;
    private String mobile;
    private String uid;
    private UserAddressModal address_first;

    public UserAddressModal getAddress_first() {
        return address_first;
    }

    public void setAddress_first(UserAddressModal address_first) {
        this.address_first = address_first;
    }


    public SubscribedUserModel() {
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public String getMobile() {
        return mobile;
    }

    public String getBalance() {
        return balance;
    }

    public Date getStart_date() {
        return start_date;
    }

    public String getDuration() {
        return duration;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
