package com.example.rapidfood.Models;

public class UserProfileModel {
    private String username;
    private String emailAddress;
    private String profileimage;
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String pMobile) {
        mobile = pMobile;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String pProfileimage) {
        profileimage = pProfileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String pEmailAddress) {
        emailAddress = pEmailAddress;
    }
}
