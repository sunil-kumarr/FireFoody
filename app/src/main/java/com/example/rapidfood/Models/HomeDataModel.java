package com.example.rapidfood.Models;

import java.util.List;

public class HomeDataModel {

    public static final int TYPE_SUBSCIPTION = 0;
    public static final int TYPE_BREAKFAST = 1;
    public static final int TYPE_PACKAGE = 2;
    public static final int TYPE_HEADER = 3;

    List<VendorDishModel> mVendorDishModelList;
    List<SubscriptionModel> mSubscriptionModelList;
    List<PackageModel> mPackageModelList;

    public List<VendorDishModel> getVendorDishModelList() {
        return mVendorDishModelList;
    }

    public void setVendorDishModelList(List<VendorDishModel> pVendorDishModelList) {
        mVendorDishModelList = pVendorDishModelList;
    }


    public List<PackageModel> getPackageModelList() {
        return mPackageModelList;
    }

    public void setPackageModelList(List<PackageModel> pPackageModelList) {
        mPackageModelList = pPackageModelList;
    }

    public List<SubscriptionModel> getSubscriptionModelList() {
        return mSubscriptionModelList;
    }

    public void setSubscriptionModelList(List<SubscriptionModel> pSubscriptionModelList) {
        mSubscriptionModelList = pSubscriptionModelList;
    }
}