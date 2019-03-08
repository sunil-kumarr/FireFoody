package com.example.rapidfood.Models;

public class VendorModel {
    private String vendorname;
    private String mobile;
    private String address;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String pEmail) {
        email = pEmail;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String pVendorname) {
        vendorname = pVendorname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String pMobile) {
        mobile = pMobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String pAddress) {
        address = pAddress;
    }
}
