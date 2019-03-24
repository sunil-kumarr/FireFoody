package com.example.rapidfood.Models;

import java.util.List;

public class HomeDataModel {

    public static final int TYPE_SUBSCIPTION = 0;
    public static final int TYPE_BREAKFAST = 1;
    public static final int TYPE_PACKAGE = 2;
    public static final int TYPE_HEADER = 3;

    List<VendorDishModel> mBreakFast;
    List<SubscriptionModel> mSubscriptionModelList;
    List<PackageModel> mPackageModelList;


}