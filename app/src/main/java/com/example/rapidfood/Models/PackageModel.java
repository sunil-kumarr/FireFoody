package com.example.rapidfood.Models;

public class PackageModel {
    String name;
    String image;
    String description;
   String price;
   String item_count;

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
