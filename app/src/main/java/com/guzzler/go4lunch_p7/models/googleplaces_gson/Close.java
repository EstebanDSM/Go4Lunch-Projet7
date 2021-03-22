package com.guzzler.go4lunch_p7.models.googleplaces_gson;


import com.google.gson.annotations.SerializedName;

public class Close {
    @SerializedName("day")
    private Long mDay;
    @SerializedName("time")
    private String mTime;

    public Long getDay() {
        return mDay;
    }

    public String getTime() {
        return mTime;
    }


}
