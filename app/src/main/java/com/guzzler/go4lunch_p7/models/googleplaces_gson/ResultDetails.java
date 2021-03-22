package com.guzzler.go4lunch_p7.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultDetails implements Comparable<ResultDetails> {


    @SerializedName("formatted_address")
    private String mFormattedAddress;
    @SerializedName("formatted_phone_number")
    private String mFormattedPhoneNumber;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("id")
    private String mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("opening_hours")
    private OpeningHours mOpeningHours;
    @SerializedName("photos")
    private List<Photo> mPhotos;
    @SerializedName("place_id")
    private String mPlaceId;
    @SerializedName("rating")
    private Double mRating;
    @SerializedName("vicinity")
    private String mVicinity;
    @SerializedName("website")
    private String mWebsite;

    private int mDistance;

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return mFormattedPhoneNumber;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public OpeningHours getOpeningHours() {
        return mOpeningHours;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public Double getRating() {
        return mRating;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public String getWebsite() {
        return mWebsite;
    }


    // SORT DISTANCE IN RECYCLERVIEW LIST OF RESTAURANTS
    @Override
    public int compareTo(ResultDetails o) {
        return Integer.compare(mDistance, ((ResultDetails) o).getDistance());
    }

}
