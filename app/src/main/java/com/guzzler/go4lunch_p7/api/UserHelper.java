package com.guzzler.go4lunch_p7.api;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.guzzler.go4lunch_p7.models.Workmate;


public class UserHelper {
    private static final String COLLECTION_NAME = "Workmates";

    // --- COLLECTION REFERENCE ---
    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---
    public static Task<Void> createWorkmate(String uid, @Nullable String urlPicture, String name) {
        if (urlPicture == null)
            urlPicture = "https://unc.nc/wp-content/uploads/2020/07/Portrait_Placeholder-1.png";
        Workmate workmateToCreate = new Workmate(uid, urlPicture, name);
        return com.guzzler.go4lunch_p7.api.UserHelper.getWorkmatesCollection().document(uid).set(workmateToCreate);
    }

    // --- GET ---
    public static Task<DocumentSnapshot> getWorkmate(String uid) {
        return com.guzzler.go4lunch_p7.api.UserHelper.getWorkmatesCollection().document(uid).get();
    }

    // --- UPDATE ---
    public static Task<Void> updateUserSettings(String userId, boolean notification) {
        return com.guzzler.go4lunch_p7.api.UserHelper.getWorkmatesCollection().document(userId).update("notification", notification);
    }
}
