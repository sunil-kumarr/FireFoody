package com.example.rapidfood.Models;

import java.util.List;

public class PackageModel {

    public static Integer getTYPE() {
        return TYPE;
    }

    final static Integer TYPE = 3;
    String name;
    String image;
    String description;
    String price;
    String item_count;
    List<VendorDishModel> dishlist;

    public List<VendorDishModel> getDishlist() {
        return dishlist;
    }

    public void setDishlist(List<VendorDishModel> pDishlist) {
        dishlist = pDishlist;
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
