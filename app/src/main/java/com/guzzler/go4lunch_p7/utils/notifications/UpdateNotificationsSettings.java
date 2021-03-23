package com.guzzler.go4lunch_p7.utils.notifications;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;

public class UpdateNotificationsSettings {

    public static void updateNotifications(Context context, NotificationHelper mNotificationHelper) {

        UserHelper.getWorkmatesCollection().document(FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener((documentSnapshot, e) -> {
            if (documentSnapshot.getData().get("notification").equals(true)) {
                mNotificationHelper.scheduleRepeatingNotification();
            } else {
                mNotificationHelper.cancelAlarmRTC();
            }
        });
    }
}
