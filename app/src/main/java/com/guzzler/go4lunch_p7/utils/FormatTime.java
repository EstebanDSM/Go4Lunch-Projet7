package com.guzzler.go4lunch_p7.utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FormatTime {

    public static String formatTime(Context context, String date) {
        date = date.substring(0, 2) + ":" + date.substring(2);
        try {
            Date date1 = new SimpleDateFormat("HH:mm", Locale.getDefault()).parse(date);
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                return dateFormat.format(date1);
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("h.mm a", Locale.getDefault());
                return dateFormat.format(date1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
