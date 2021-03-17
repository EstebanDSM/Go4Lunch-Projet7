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

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return dist;
    }

    public static String distFrom2(String startLocation, Location endLocation) {
        String[] separatedStart = startLocation.split(",");
        double startLat = Double.parseDouble(separatedStart[0]);
        double startLng = Double.parseDouble(separatedStart[1]);
        double earthRadius = 3958.75;
        double lat2 = endLocation.getLat();
        double lng2 = endLocation.getLng();
        double dLat = Math.toRadians(lat2 - startLat);
        double dLng = Math.toRadians(lng2 - startLng);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return String.valueOf(Math.round(earthRadius * c));
    }


}
