package com.rapdfoods.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class VendorDishModel implements Parcelable {

    String name;
    String description;
    String image;
    List<String> packlist;

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String pDescription) {
        description = pDescription;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String pImage) {
        image = pImage;
    }


    public List<String> getPacklist() {
        return packlist;
    }

    public void setPacklist(List<String> pPacklist) {
        packlist = pPacklist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}