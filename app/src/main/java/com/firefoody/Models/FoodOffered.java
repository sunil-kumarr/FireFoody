package com.firefoody.Models;

import java.util.List;

public class FoodOffered {
    PackageModel foodPack;

    public PackageModel getFoodPack() {
        return foodPack;
    }

    public List<VendorDishModel> getmCurrentMenuList() {
        return mCurrentMenuList;
    }

    public void setmCurrentMenuList(List<VendorDishModel> mCurrentMenuList) {
        this.mCurrentMenuList = mCurrentMenuList;
    }

    public void setFoodPack(PackageModel foodPack) {
        this.foodPack = foodPack;
    }

    List<VendorDishModel> mCurrentMenuList;

}
