package com.example.rapidfood.Models;

import java.util.List;

public class BreakfastContainerModel {
    String type;

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    List<VendorDishModel> breakfastlist;

    public List<VendorDishModel> getBreakfastlist() {
        return breakfastlist;
    }

    public void setBreakfastlist(List<VendorDishModel> pBreakfastlist) {
        breakfastlist = pBreakfastlist;
    }
}
