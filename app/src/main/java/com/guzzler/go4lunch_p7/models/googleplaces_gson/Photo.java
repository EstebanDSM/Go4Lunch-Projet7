package com.guzzler.go4lunch_p7.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;


public class Photo {
    @SerializedName("height")
    private Long mHeight;
    @SerializedName("photo_reference")
    private String mPhotoReference;
    @SerializedName("width")
    private Long mWidth;

    public Long getHeight() {
        return mHeight;
    }

    public void setHeight(Long height) {
        mHeight = height;
    }

    public String getPhotoReference() {
        return mPhotoReference;
    }

    public Long getWidth() {
        return mWidth;
    }

    public void setWidth(Long width) {
        mWidth = width;
    }
}