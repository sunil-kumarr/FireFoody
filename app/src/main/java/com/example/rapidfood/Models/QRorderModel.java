package com.example.rapidfood.Models;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class QRorderModel implements Serializable {
    @SerializedName("package")
    @Expose
    private String pack_name;
    @SerializedName("packprice")
    @Expose
    private String pack_price;
    @SerializedName("userUID")
    @Expose
    private String  user_UID;
    @SerializedName("mobile")
    @Expose
    private String user_mobile;
    @SerializedName("qrId")
    @Expose
    private String qr_id;


    @SerializedName("timestamp")
    @Expose
    @ServerTimestamp
    private Date timestamp;


    public String getQr_id() {
        return qr_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setQr_id(String pQr_id) {
        qr_id = pQr_id;
    }

    public String getPack_name() {
        return pack_name;
    }

    public void setPack_name(String pPack_name) {
        pack_name = pPack_name;
    }

    public String getPack_price() {
        return pack_price;
    }

    public void setPack_price(String pPack_price) {
        pack_price = pPack_price;
    }

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String pUser_UID) {
        user_UID = pUser_UID;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String pUser_mobile) {
        user_mobile = pUser_mobile;
    }
}
