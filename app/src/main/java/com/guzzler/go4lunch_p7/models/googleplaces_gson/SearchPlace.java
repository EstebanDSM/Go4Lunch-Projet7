package com.guzzler.go4lunch_p7.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SearchPlace {
    @SerializedName("html_attributions")
    private List<Object> mHtmlAttributions;
    @SerializedName("results")
    private List<ResultSearch> mResultSearches;
    @SerializedName("status")
    private String mStatus;

    public List<Object> getHtmlAttributions() {
        return mHtmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        mHtmlAttributions = htmlAttributions;
    }

    public List<ResultSearch> getResultSearches() {
        return mResultSearches;
    }

    public void setResultSearches(List<ResultSearch> resultSearches) {
        mResultSearches = resultSearches;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}
