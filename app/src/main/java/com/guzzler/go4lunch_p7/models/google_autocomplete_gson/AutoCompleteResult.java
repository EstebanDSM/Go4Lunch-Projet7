package com.guzzler.go4lunch_p7.models.google_autocomplete_gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class AutoCompleteResult {
    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions;


    public List<Prediction> getPredictions() {
        return predictions;
    }
}