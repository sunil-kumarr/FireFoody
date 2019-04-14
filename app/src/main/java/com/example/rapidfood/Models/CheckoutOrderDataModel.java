package com.example.rapidfood.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CheckoutOrderDataModel implements Serializable {
    @SerializedName("dishlist")
    @Expose
    private List<String> orderDishlist;
    @SerializedName("packName")
    @Expose
    private String packageName;

    public List<String> getOrderDishlist() {
        return orderDishlist;
    }

    public void setOrderDishlist(List<String> pOrderDishlist) {
        orderDishlist = pOrderDishlist;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String pPackageName) {
        packageName = pPackageName;
    }
}
