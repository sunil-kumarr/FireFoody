package com.example.rapidfood.Models;

import java.util.ArrayList;

public class HomeMenuModel {

    public static final int SUBSCRIPTION_TYPE = 1;
    public static final int MENUITEM_TYPE = 2;

    private int viewType;

    public int getViewType() {
        return viewType;
    }

    private ArrayList<String> subscriptionImage;
    private ArrayList<VendorMenuItem> mMenuItems;

    public ArrayList<String> getSubscriptionImage() {
        return subscriptionImage;
    }

    public void setSubscriptionImage(ArrayList<String> pSubscriptionImage) {
        subscriptionImage = pSubscriptionImage;
    }

    public ArrayList<VendorMenuItem> getMenuItems() {
        return mMenuItems;
    }

    public void setMenuItems(ArrayList<VendorMenuItem> pMenuItems) {
        mMenuItems = pMenuItems;
    }
}
