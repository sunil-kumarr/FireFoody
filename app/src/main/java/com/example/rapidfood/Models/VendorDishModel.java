package com.example.rapidfood.Models;

import java.util.List;

public class VendorDishModel implements Comparable<VendorDishModel>{

    final static Integer TYPE=2;

    String name;
    String description;
    String image;
     String money;
    Integer itemcategory;
    List<String> packlist;

    public String getMoney() {
        return money;
    }

    public void setMoney(String pMoney) {
        money = pMoney;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String pImage) {
        image = pImage;
    }

    public Integer getItemcategory() {
        return itemcategory;
    }

    public void setItemcategory(Integer pItemcategory) {
        itemcategory = pItemcategory;
    }

    public List<String> getPacklist() {
        return packlist;
    }

    public void setPacklist(List<String> pPacklist) {
        packlist = pPacklist;
    }

    @Override
    public int compareTo(VendorDishModel dish) {
        return (this.getItemcategory() < dish.getItemcategory() ? -1 :
            (this.getItemcategory().equals(dish.getItemcategory())? 0 : 1));
    }
}
