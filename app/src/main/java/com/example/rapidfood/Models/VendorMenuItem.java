package com.example.rapidfood.Models;

public class VendorMenuItem {
    private String Itemname;
    private String Itemdescription;
    private String s1, s2, s3, s4;
    private String ItemImageid;
    private String ItemCategory;

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String pItemCategory) {
        ItemCategory = pItemCategory;
    }

    public VendorMenuItem() {
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String pS1) {
        s1 = pS1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String pS2) {
        s2 = pS2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String pS3) {
        s3 = pS3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String pS4) {
        s4 = pS4;
    }

    public String getItemname() {
        return Itemname;
    }

    public void setItemname(String pItemname) {
        Itemname = pItemname;
    }

    public String getItemdescription() {
        return Itemdescription;
    }

    public void setItemdescription(String pItemdescription) {
        Itemdescription = pItemdescription;
    }


    public String getItemImageid() {
        return ItemImageid;
    }

    public void setItemImageid(String pItemImageid) {
        ItemImageid = pItemImageid;
    }
}
