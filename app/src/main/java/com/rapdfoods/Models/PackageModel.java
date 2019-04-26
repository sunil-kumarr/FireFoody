package com.rapdfoods.Models;

import java.util.List;

public class PackageModel {


    private String name;
    private String image;
    private String description;
    private String price;
    private String item_count;
    private boolean isBreakfast;
    private List<VendorDishModel> dishlist;

    public List<VendorDishModel> getDishlist() {
        return dishlist;
    }

    public void setDishlist(List<VendorDishModel> pDishlist) {
        dishlist = pDishlist;
    }

    public boolean isBreakfast() {
        return isBreakfast;
    }

    public void setBreakfast(boolean pBreakfast) {
        isBreakfast = pBreakfast;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String pImage) {
        image = pImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String pPrice) {
        price = pPrice;
    }

    public String getItem_count() {
        return item_count;
    }

    public void setItem_count(String pItem_count) {
        item_count = pItem_count;
    }

}
