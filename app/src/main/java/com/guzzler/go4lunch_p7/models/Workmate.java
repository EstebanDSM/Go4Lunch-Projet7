package com.guzzler.go4lunch_p7.models;

import androidx.annotation.Nullable;


public class Workmate {
    private String uid;
    private String urlPicture;
    private String name;
    private boolean notification;


    // Constructeur vide pour la recyclerView // TODO : pourquoi ?
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

}
