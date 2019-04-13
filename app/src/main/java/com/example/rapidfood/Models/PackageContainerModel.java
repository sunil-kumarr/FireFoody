package com.example.rapidfood.Models;

import java.util.List;

public class PackageContainerModel extends PackageModel {
    private List<VendorDishModel> dishlist;

    public List<VendorDishModel> getDishlist() {
        return dishlist;
    }

    public void setDishlist(List<VendorDishModel> pDishlist) {
        dishlist = pDishlist;
    }
}
