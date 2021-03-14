package com.guzzler.go4lunch_p7.models.googleplaces_gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class OpeningHours {
    @SerializedName("open_now")
    private Boolean mOpenNow;
    @SerializedName("periods")
    private List<Period> mPeriods;
    @SerializedName("weekday_text")
    private List<String> mWeekdayText;

    public Boolean getOpenNow() {
        return mOpenNow;
    }

    public void setOpenNow(Boolean openNow) {
        mOpenNow = openNow;
    }

    public List<Period> getPeriods() {
        return mPeriods;
    }

    public void setPeriods(List<Period> periods) {
        mPeriods = periods;
    }

    public List<String> getWeekdayText() {
        return mWeekdayText;
    }

    public void setWeekdayText(List<String> weekdayText) {
        mWeekdayText = weekdayText;
    }
}
