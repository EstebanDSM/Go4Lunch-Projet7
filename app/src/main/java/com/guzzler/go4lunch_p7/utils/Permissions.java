package com.guzzler.go4lunch_p7.utils;

import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

public class Permissions {

    public static void checkLocationPermission(Context context) {
        EasyPermissions.hasPermissions(context, Constants.PERMISSIONS);
    }
}
