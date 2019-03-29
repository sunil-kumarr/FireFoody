package com.example.rapidfood.Models;

import java.util.List;

public class SubscriptionContainerModel {

    String type;

    public String getType() {
        return type;
    }

    public void setType(String pType) {
        type = pType;
    }

    List<SubscriptionModel> subscriptionlist;

    public List<SubscriptionModel> getSubscriptionlist() {
        return subscriptionlist;
    }

    public void setSubscriptionlist(List<SubscriptionModel> pSubscriptionlist) {
        subscriptionlist = pSubscriptionlist;
    }
}
