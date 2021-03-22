package com.guzzler.go4lunch_p7.models.googleplaces_gson;


import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location mLocation;

    public Location getLocation() {
        return mLocation;
    }


}
