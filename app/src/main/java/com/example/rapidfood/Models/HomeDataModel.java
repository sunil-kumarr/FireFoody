package com.example.rapidfood.Models;

import java.util.List;

public class HomeDataModel {

    public static final int TYPE_SUBSCIPTION = 0;
    public static final int TYPE_PACKAGE = 1;
    List<PackageModel> mPackageModelList;
    SubscriptionContainerModel mSubscriptionContainerModel;

    public List<PackageModel> getPackageModelList() {
        return mPackageModelList;
    }

    public void setPackageModelList(List<PackageModel> pPackageModelList) {
        mPackageModelList = pPackageModelList;
    }

    public SubscriptionContainerModel getSubscriptionContainerModel() {
        return mSubscriptionContainerModel;
    }

    public void setSubscriptionContainerModel(SubscriptionContainerModel pSubscriptionContainerModel) {
        mSubscriptionContainerModel = pSubscriptionContainerModel;
    }
}