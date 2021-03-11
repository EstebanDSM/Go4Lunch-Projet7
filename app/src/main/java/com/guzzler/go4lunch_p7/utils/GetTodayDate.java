package com.guzzler.go4lunch_p7.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GetTodayDate {

    public static String getTodayDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sfDate = new SimpleDateFormat("d MMM yyyy",
                Locale.getDefault());
        return sfDate.format(c.getTime());
    }
}
