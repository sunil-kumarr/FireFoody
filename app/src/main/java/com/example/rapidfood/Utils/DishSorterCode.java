package com.example.rapidfood.Utils;

import com.example.rapidfood.Models.VendorDishModel;

import java.util.ArrayList;
import java.util.Collections;

public class DishSorterCode {

    ArrayList<VendorDishModel> dishList = new ArrayList<>();

    public DishSorterCode(ArrayList<VendorDishModel> pDishList) {
        this.dishList = pDishList;
    }
    public ArrayList<VendorDishModel> getDishList() {
        Collections.sort(dishList);
        return dishList;
    }
}
