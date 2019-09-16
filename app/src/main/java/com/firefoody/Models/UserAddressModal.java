package com.firefoody.Models;

public class UserAddressModal {
    private String addressname;
    private String addresscomplete;
    private String addressinstructions;
    private String customername;
    private String mobilenumber;

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getAddressname() {
        return addressname;
    }

    public void setAddressname(String pAddressname) {
        addressname = pAddressname;
    }

    public String getAddresscomplete() {
        return addresscomplete;
    }

    public void setAddresscomplete(String pAddresscomplete) {
        addresscomplete = pAddresscomplete;
    }

    public String getAddressinstructions() {
        return addressinstructions;
    }

    public void setAddressinstructions(String pAddressinstructions) {
        addressinstructions = pAddressinstructions;
    }
}
