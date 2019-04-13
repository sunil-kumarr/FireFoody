package com.example.rapidfood.Models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class SubscriptionTransactionModel {
    private String subname;
    private String subcost;
    private String subcoupon;
    private String total_paid;
    private String duration;
    @ServerTimestamp
    private Date transaction_time;
    private String mobile;
    private String uid;
    private String transaction_id;
    private String googlePay_status;

    public String getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(String pTotal_paid) {
        total_paid = pTotal_paid;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String pSubname) {
        subname = pSubname;
    }

    public String getSubcost() {
        return subcost;
    }

    public void setSubcost(String pSubcost) {
        subcost = pSubcost;
    }

    public String getSubcoupon() {
        return subcoupon;
    }

    public void setSubcoupon(String pSubcoupon) {
        subcoupon = pSubcoupon;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String pDuration) {
        duration = pDuration;
    }

    public Date getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(Date pTransaction_time) {
        transaction_time = pTransaction_time;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String pMobile) {
        mobile = pMobile;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String pUid) {
        uid = pUid;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String pTransaction_id) {
        transaction_id = pTransaction_id;
    }

    public String getGooglePay_status() {
        return googlePay_status;
    }

    public void setGooglePay_status(String pGooglePay_status) {
        googlePay_status = pGooglePay_status;
    }
}