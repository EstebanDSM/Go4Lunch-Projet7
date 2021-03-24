package com.guzzler.go4lunch_p7.utils;

import android.Manifest;

import com.guzzler.go4lunch_p7.BuildConfig;
import com.guzzler.go4lunch_p7.ui.map.Map_Fragment;


public class Constants {

    // TODO : on peut mettre la clef en public ?
    public static final String API_KEY = BuildConfig.api_key;


    public static final int EARTH_RADIUS_KM = 6371;
    public static final int ALARM_TYPE_RTC = 100;
    public static final String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";
    public static final String CLOSING_SOON = "CLOSING_SOON";
    public static final String OPENING_HOURS_NOT_KNOW = "OPENING_HOURS_NOT_KNOW";
    public static final String BASE_URL_PLACE_PHOTO = "https://maps.googleapis.com/maps/api/place/photo";
    public static final int MAX_WIDTH = 200;
    public static final int MAX_HEIGHT = 200;
    public static final String TAG = Map_Fragment.class.getSimpleName();
    public static final int DEFAULT_ZOOM = 13;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final String COLLECTION_BOOKING = "booking";
    public static final String COLLECTION_LIKED_NAME = "restaurantLiked";
    public static final String COLLECTION_WORKMATE = "Workmates";
    public static final String BASE_URL_GOOGLE_API = "https://maps.googleapis.com";
    public static final int RADIUS = 6800;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String NOTIFICATION_CHANNEL_NAME = "Go4Lunch";
    public static final int MAX_WIDTH_LARGE = 400;
    public static final int MAX_HEIGHT_LARGE = 400;

}

