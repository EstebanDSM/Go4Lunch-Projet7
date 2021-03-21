package com.guzzler.go4lunch_p7.models;

import androidx.annotation.Nullable;

import com.guzzler.go4lunch_p7.api.firebase.UserHelper;

import java.util.concurrent.atomic.AtomicBoolean;


public class Workmate {
    private String uid;
    private String urlPicture;
    private String name;
    private boolean notification;


    // Constructeur vide pour la recyclerView
    public Workmate() {
    }

    public Workmate(String uid, @Nullable String urlPicture, String name) {
        this.uid = uid;
        this.urlPicture = urlPicture;
        this.name = name;
        this.notification = false;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Nullable
    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(@Nullable String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public void checkIf_UID_Exist(String string_UID) {
        AtomicBoolean exist = new AtomicBoolean(false);
        UserHelper.getWorkmate(string_UID).addOnCompleteListener(UserTask -> {
                    if (UserTask.isSuccessful()) {
                        exist.set(UserTask.getResult().contains(string_UID));
                    }
                }
        );
    }
}
