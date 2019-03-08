package com.example.rapidfood.Models;

public class SubscriptionModel {
    private String duration;
    private String type;
    private String price;
    private String details;
    private String imagesub;

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
        return price;
    }

    public void setPrice(String pPrice) {
        price = pPrice;
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
