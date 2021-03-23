package com.guzzler.go4lunch_p7.utils;

import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.ui.MainActivity;

import static com.guzzler.go4lunch_p7.utils.Constants.EARTH_RADIUS_KM;

public class DistanceTo {


    public static double distanceTo(ResultDetails resultDetails, MainActivity mMainActivity) {
        double lat1Rad = Math.toRadians(mMainActivity.mShareViewModel.currentUserPosition.getValue().latitude);
        double lat2Rad = Math.toRadians(resultDetails.getGeometry().getLocation().getLat());
        double deltaLonRad = Math.toRadians(resultDetails.getGeometry().getLocation().getLng() - mMainActivity.mShareViewModel.currentUserPosition.getValue().longitude);

        return 1000 * Math.acos(
                Math.sin(lat1Rad) * Math.sin(lat2Rad) +
                        Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.cos(deltaLonRad)
        ) * EARTH_RADIUS_KM;
    }
}
