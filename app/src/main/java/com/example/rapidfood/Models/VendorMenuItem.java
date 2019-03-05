package com.example.rapidfood.Models;

public class VendorMenuItem {
    private  String Itemname;
    private String Itemdescription;
    private  String Itemquantitiy;
    private String Itempriority;
    private String ItemImageid;

    public VendorMenuItem() {
    }

    public VendorMenuItem(String pItemname, String pItemdescription, String pItemquantitiy, String pItempriority, String pItemImageid) {
        Itemname = pItemname;
        Itemdescription = pItemdescription;
        Itemquantitiy = pItemquantitiy;
        Itempriority = pItempriority;
        ItemImageid = pItemImageid;
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

    public String getItemquantitiy() {
        return Itemquantitiy;
    }

    public void setItemquantitiy(String pItemquantitiy) {
        Itemquantitiy = pItemquantitiy;
    }

    public String getItempriority() {
        return Itempriority;
    }

    public void setItempriority(String pItempriority) {
        Itempriority = pItempriority;
    }

    public String getItemImageid() {
        return ItemImageid;
    }

    public void setItemImageid(String pItemImageid) {
        ItemImageid = pItemImageid;
    }
}
