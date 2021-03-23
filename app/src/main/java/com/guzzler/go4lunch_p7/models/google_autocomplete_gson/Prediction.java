package com.guzzler.go4lunch_p7.models.google_autocomplete_gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Prediction {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("place_id")
    @Expose
    private String placeId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }


}
