package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

public class CheckoutPlaceOrderModel {
    String packageordered;
    String packageprice;
    String deliveryaddress;
    List<String> selecteditems;
    String uid;
    String mobile;
    boolean custom;
    @ServerTimestamp
    Date ordertimestamp;
    String trans_id;
    String paymentmethod;
    String paymentstatus;
    String deliverystatus;


    public String getUid() {
        return uid;
    }

    public void setUid(String pUid) {
        uid = pUid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String pMobile) {
        mobile = pMobile;
    }

    public String getPackageordered() {
        return packageordered;
    }

    public void setPackageordered(String pPackageordered) {
        packageordered = pPackageordered;
    }

    public String getPackageprice() {
        return packageprice;
    }

    public void setPackageprice(String pPackageprice) {
        packageprice = pPackageprice;
    }

    public String getDeliveryaddress() {
        return deliveryaddress;
    }

    public void setDeliveryaddress(String pDeliveryaddress) {
        deliveryaddress = pDeliveryaddress;
    }

    public List<String> getSelecteditems() {
        return selecteditems;
    }

    public void setSelecteditems(List<String> pSelecteditems) {
        selecteditems = pSelecteditems;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean pCustom) {
        custom = pCustom;
    }

    public Date getOrdertimestamp() {
        return ordertimestamp;
    }


    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String pTrans_id) {
        trans_id = pTrans_id;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String pPaymentmethod) {
        paymentmethod = pPaymentmethod;
    }

    public String getPaymentstatus() {
        return paymentstatus;
    }

    public void setPaymentstatus(String pPaymentstatus) {
        paymentstatus = pPaymentstatus;
    }

    public String getDeliverystatus() {
        return deliverystatus;
    }

    public void setDeliverystatus(String pDeliverystatus) {
        deliverystatus = pDeliverystatus;
    }
}
