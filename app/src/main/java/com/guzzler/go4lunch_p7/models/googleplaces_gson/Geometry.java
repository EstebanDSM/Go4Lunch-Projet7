package com.guzzler.go4lunch_p7.models.googleplaces_gson;


import com.google.gson.annotations.SerializedName;

public class Geometry {
    @SerializedName("location")
    private Location mLocation;
    @SerializedName("viewport")
    private Viewport mViewport;

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public void setViewport(Viewport viewport) {
        mViewport = viewport;
    }
}
