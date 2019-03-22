package com.example.rapidfood.Models;

public class HomeDataModel {
    SubscriptionModel mSubscriptionModel;
    PackageModel mPackageModel;
    VendorBreakFastItem mVendorBreakFastItem;
    VendorDishModel mVendorDishModel;

    public SubscriptionModel getSubscriptionModel() {
        return mSubscriptionModel;
    }

    public void setSubscriptionModel(SubscriptionModel pSubscriptionModel) {
        mSubscriptionModel = pSubscriptionModel;
    }

    public PackageModel getPackageModel() {
        return mPackageModel;
    }

    public void setPackageModel(PackageModel pPackageModel) {
        mPackageModel = pPackageModel;
    }

    public VendorBreakFastItem getVendorBreakFastItem() {
        return mVendorBreakFastItem;
    }

    public void setVendorBreakFastItem(VendorBreakFastItem pVendorBreakFastItem) {
        mVendorBreakFastItem = pVendorBreakFastItem;
    }

    public VendorDishModel getVendorDishModel() {
        return mVendorDishModel;
    }

    public void setVendorDishModel(VendorDishModel pVendorDishModel) {
        mVendorDishModel = pVendorDishModel;
    }
}
