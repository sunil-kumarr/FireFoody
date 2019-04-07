package com.example.rapidfood.GooglePay;

public class ItemInfo {
    private final String name;
    // Micros are used for prices to avoid rounding errors when converting between currencies.
    private final long priceMicros;
    private final String details;
    private  final String duration;

    public ItemInfo(String pName, long pPriceMicros, String pDetails, String pDuration) {
        name = pName;
        priceMicros = pPriceMicros;
        details = pDetails;
        duration = pDuration;
    }

    public String getDetails() {
        return details;
    }

    public String getName() {
        return name;
    }

    public long getPriceMicros() {
        return priceMicros;
    }

    public String getDuration() {
        return duration;
    }
}
