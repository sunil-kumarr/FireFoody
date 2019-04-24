package com.example.rapidfood.Models;

import com.google.gson.annotations.SerializedName;

public class ChecksumResponse {
    @SerializedName("CHECKSUMHASH")
    public String CHECKSUMHASH;
    @SerializedName("ORDER_ID")
    public String ORDER_ID;
    @SerializedName("payt_STATUS")
    public String payt_STATUS;

}