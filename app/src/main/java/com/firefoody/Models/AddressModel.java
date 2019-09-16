package com.firefoody.Models;

public class AddressModel {
    private String itemName;
    private Integer itemPrice;
    private Integer type;
    public static final int TYPE_ADD = 1;
    public static final int TYPE_ADDRESS = 0;

     AddressModel(String name, Integer price, Integer type) {
        this.itemName = name;
        this.itemPrice = price;
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public String getItemName() {
        return itemName;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }
}
