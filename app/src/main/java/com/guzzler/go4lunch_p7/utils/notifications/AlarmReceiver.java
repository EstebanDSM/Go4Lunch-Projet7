package com.guzzler.go4lunch_p7.utils.notifications;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;

import java.util.List;

import static com.guzzler.go4lunch_p7.utils.Constants.NOTIFICATION_CHANNEL_ID;


public class AlarmReceiver extends BroadcastReceiver implements GooglePlaceDetailsCalls.Callbacks {

    private NotificationCompat.Builder mBuilder;
    private List<String> workmatesList;
    private Context mContext;


    @Override
    public void onReceive(Context context, Intent intent) {
//        this.mContext = context;
//        workmatesList = new ArrayList<>();
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            RestaurantsHelper.getBooking(FirebaseAuth.getInstance().getCurrentUser().getUid(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
//                if (restaurantTask.isSuccessful()) {
//                    if (!(restaurantTask.getResult().isEmpty())) { //
//                        Log.e("TAG", "onReceive: Sending notifications");
//                        for (DocumentSnapshot restaurant : restaurantTask.getResult()) {
//                            RestaurantsHelper.getTodayBooking(restaurant.getData().get("restaurantId").toString(), getTodayDate()).addOnCompleteListener(bookingTask -> {
//                                if (bookingTask.isSuccessful()) {
//                                    for (QueryDocumentSnapshot booking : bookingTask.getResult()) {
//                                        UserHelper.getWorkmate(booking.getData().get("userId").toString()).addOnCompleteListener(userTask -> {
//                                            if (userTask.isSuccessful()) {
//                                                if (!(userTask.getResult().getData().get("uid").toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
//                                                    Log.e("TAG", "ALARM_RECEIVER | User : " + userTask.getResult().getData().get("name"));
//                                                    String username = userTask.getResult().getData().get("name").toString();
//                                                    workmatesList.add(username);
//                                                }
//                                            }
//                                            if (workmatesList.size() == bookingTask.getResult().size() - 1) {
//                                                GooglePlaceDetailsCalls.fetchPlaceDetails(this, restaurant.getData().get("restaurantId").toString());
//                                            }
//                                        });
//                                    }
//                                    Log.e("TAG", "onReceive: " + workmatesList.toString());
//                                }
//                            });
//                        }
//                    } else {
//                        Log.e("TAG", "onReceive: No booking for this user today");
//                    }
//                }
//            });
//        }
    }

    public void sendNotification(String workmates) {
//        Log.e("TAG", "sendNotification: USERS " + workmates);
//        Intent resultIntent = new Intent(mContext, MainActivity.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NotificationHelper.ALARM_TYPE_RTC, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Notification repeatedNotification = buildLocalNotification(mContext, pendingIntent, workmates).build();
//        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.RED);
//            notificationChannel.enableVibration(true);
//            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//        notificationManager.notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context mContext, PendingIntent pendingIntent, String workmates) {
        Log.e("TAG", "buildLocalNotification: USERS " + workmates);
        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.notification))
                .setContentText(mContext.getResources().getString(R.string.notification_message))
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(workmates))
                .setAutoCancel(true);

        return mBuilder;
    }


    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        String restaurantName = resultDetails.getName();

        if (workmatesList.size() > 0) {
            StringBuilder mStringBuilder = new StringBuilder();
            for (int i = 0; i < workmatesList.size(); i++) {
                mStringBuilder.append(workmatesList.get(i));
                if (!(i == workmatesList.size() - 1)) {
                    mStringBuilder.append(", ");
                }
            }
            sendNotification(mContext.getResources().getString(
                    R.string.notification_message,
                    restaurantName,
                    mStringBuilder));
        } else {
            sendNotification(mContext.getResources().getString(
                    R.string.notification_message,
                    restaurantName,
                    mContext.getResources().getString(R.string.notification_no_workmates)));
        }
    }

    @Override
    public void onFailure() {
    }
}