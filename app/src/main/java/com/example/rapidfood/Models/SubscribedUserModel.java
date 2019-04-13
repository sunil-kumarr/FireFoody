package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class SubscribedUserModel {
    private String balance;
    @ServerTimestamp
    private Date start_date;
    private String duration;
    private String trans_id;

    public SubscribedUserModel(String pBalance) {
        balance = pBalance;
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
}
