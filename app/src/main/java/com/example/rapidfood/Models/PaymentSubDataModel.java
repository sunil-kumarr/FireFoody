package com.example.rapidfood.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentSubDataModel implements Serializable {


    @SerializedName("txn_amount")
    @Expose
    private
    String amount;
    @SerializedName("cust_id")
    @Expose
    private
    String cust_id;
    @SerializedName("order_id")
    @Expose
    private
    String order_id;
    @SerializedName("cust_mob")
    @Expose
    private
    String mobile;

    @SerializedName("sub_name")
    @Expose
    private
    String subname;
    @SerializedName("sub_duration")
    @Expose
    private
    String duration;
    @SerializedName("sub_coupon")
    @Expose
    private
    String subcoupon;
    @SerializedName("sub_cost")
    @Expose
    private
    String subcost;


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSubname() {
        return subname;
    }

    public void setSubname(String subname) {
        this.subname = subname;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSubcoupon() {
        return subcoupon;
    }

    public void setSubcoupon(String subcoupon) {
        this.subcoupon = subcoupon;
    }

    public String getSubcost() {
        return subcost;
    }

    public void setSubcost(String subcost) {
        this.subcost = subcost;
    }
}
