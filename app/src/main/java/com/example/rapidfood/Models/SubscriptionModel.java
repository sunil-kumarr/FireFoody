package com.example.rapidfood.Models;

import java.io.Serializable;

public class SubscriptionModel {


    private String duration;
    private String type;
    private String mPrice;
    private String details;
    private String imagesub;
    private String coupon;

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String pCoupon) {
        coupon = pCoupon;
    }

    public String getImagesub() {
        return imagesub;
    }

    public void setImagesub(String pImagesub) {
        imagesub = pImagesub;
    }

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String pPrice) {
        mPrice = pPrice;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String pDetails) {
        details = pDetails;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String pDuration) {
        duration = pDuration;
    }
}
