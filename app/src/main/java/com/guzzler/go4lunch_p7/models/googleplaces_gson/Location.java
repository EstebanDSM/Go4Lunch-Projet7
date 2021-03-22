package com.guzzler.go4lunch_p7.models.googleplaces_gson;


import com.google.gson.annotations.SerializedName;


public class Location {
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lng")
    private Double mLng;

    public Double getLat() {
        return mLat;
    }

    public Double getLng() {
        return mLng;
    }


}
