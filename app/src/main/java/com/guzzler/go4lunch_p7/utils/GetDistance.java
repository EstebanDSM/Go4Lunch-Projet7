package com.guzzler.go4lunch_p7.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.Location;

public class GetDistance {

    public static String getDistance(String startLocation, Location endLocation) {
        String[] separatedStart = startLocation.split(",");
        double startLatitude = Double.parseDouble(separatedStart[0]);
        double startLongitude = Double.parseDouble(separatedStart[1]);
        LatLng location = new LatLng(startLatitude, startLongitude);
        return String.valueOf(Math.round(SphericalUtil.computeDistanceBetween(location, new LatLng(endLocation.getLat(), endLocation.getLng()))));
    }

}
