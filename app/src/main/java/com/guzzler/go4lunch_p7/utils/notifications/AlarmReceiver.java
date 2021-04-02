package com.guzzler.go4lunch_p7.utils.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.RestaurantsHelper;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.api.retrofit.googleplace.GooglePlaceDetailsCalls;
import com.guzzler.go4lunch_p7.models.googleplaces_gson.ResultDetails;
import com.guzzler.go4lunch_p7.ui.MainActivity;
import com.guzzler.go4lunch_p7.utils.Constants;
import com.guzzler.go4lunch_p7.utils.ShowToastSnack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.guzzler.go4lunch_p7.utils.Constants.NOTIFICATION_CHANNEL_ID;
import static com.guzzler.go4lunch_p7.utils.Constants.NOTIFICATION_CHANNEL_NAME;
import static com.guzzler.go4lunch_p7.utils.GetTodayDate.getTodayDate;


public class AlarmReceiver extends BroadcastReceiver implements GooglePlaceDetailsCalls.Callbacks {

    String restaurantName;
    private NotificationCompat.Builder mBuilder;
    private List<String> workmatesList;
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        workmatesList = new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            // On recherche si l'utilisateur actuel a réservé un restau
            RestaurantsHelper.getBooking(FirebaseAuth.getInstance().getCurrentUser().getUid(), getTodayDate()).addOnCompleteListener(restaurantTask -> {
                // Si la requête est terminée
                if (restaurantTask.isSuccessful()) {
                    // Si l'utilisateur actuel a bien réservé aujourd'hui
                    if (!(restaurantTask.getResult().isEmpty())) {
                        Log.e("TAG", restaurantTask.getResult().toString());
                        // Dans le résultat de la tâche precédente
                        for (DocumentSnapshot restaurant : restaurantTask.getResult()) {
                            // Dans toutes les réservations, on recherche celles dans le même restau que l'utilisateur actuel
                            RestaurantsHelper.getTodayBooking(Objects.requireNonNull(Objects.requireNonNull(restaurant.getData()).get("restaurantId")).toString(), getTodayDate()).addOnCompleteListener(bookingTask -> {
                                // Si la requête est terminée
                                if (bookingTask.isSuccessful()) {
                                    // Dans le résultat de cette requête
                                    for (QueryDocumentSnapshot booking : bookingTask.getResult()) {
                                        // On recherche Les utilisateurs correspondants (grâce aux uid récupérés dans bookingTask)
                                        UserHelper.getWorkmate(Objects.requireNonNull(booking.getData().get("userId")).toString()).addOnCompleteListener(userTask -> {
                                            // Si la requête est terminée
                                            if (userTask.isSuccessful()) {
                                                // Si l'uid ne correspond PAS à celui de l'utilisateur actuel
                                                if (!(Objects.requireNonNull(Objects.requireNonNull(userTask.getResult().getData()).get("uid")).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) {
                                                    Log.e("TAG", "ALARM_RECEIVER | User : " + userTask.getResult().getData().get("name"));
                                                    // On rajoute son nom dans la liste
                                                    String username = Objects.requireNonNull(userTask.getResult().getData().get("name")).toString();
                                                    workmatesList.add(username);
                                                }
                                            }
                                            // Lorsque tous les utilisateurs ayant réservé le même restau sont ajoutés à la liste (moins l'utilisateur actuel)
                                            if (workmatesList.size() == bookingTask.getResult().size() - 1) {
                                                // On lance une requête web sur les détails de ce restau
                                                GooglePlaceDetailsCalls.fetchPlaceDetails(this, Objects.requireNonNull(restaurant.getData().get("restaurantId")).toString());
                                            }
                                        });
                                    }
                                    Log.e("TAG", "onReceive: " + workmatesList.toString());
                                }
                            });
                        }
                        // L'utilisateur actuel n'a rien réservé aujourd'hui
                    } else {
                        Log.e("TAG", "onReceive: No booking for this user today");
                    }
                }
            });
        }
    }


    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        assert resultDetails != null;
        restaurantName = resultDetails.getName();
        sendNotification(MakeMessage.textMessage(workmatesList, mContext, restaurantName));
    }


    public void sendNotification(String workmates) {
        Log.e("TAG", "sendNotification: USERS " + workmates);
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, Constants.ALARM_TYPE_RTC, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification repeatedNotification = buildLocalNotification(mContext, pendingIntent, workmates).build();
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(Constants.ALARM_TYPE_RTC, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context mContext, PendingIntent pendingIntent, String workmates) {
        Log.e("TAG", "buildLocalNotification: USERS " + workmates);
        mBuilder = new NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.bowl);
        mBuilder.setContentTitle(mContext.getResources().getString(R.string.notification))
                .setContentText(MakeMessage.textMessage(workmatesList, mContext, restaurantName))
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(workmates))
                .setAutoCancel(true);
        return mBuilder;
    }


    @Override
    public void onFailure() {
        ShowToastSnack.showToast(mContext, "Error on retrieve restaurant's details", 0);
    }
}