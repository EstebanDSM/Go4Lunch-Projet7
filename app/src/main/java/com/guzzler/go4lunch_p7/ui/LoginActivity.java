package com.guzzler.go4lunch_p7.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.databinding.ActivityLoginBinding;

import java.util.Arrays;
import java.util.Objects;

import static com.guzzler.go4lunch_p7.utils.ShowToastSnack.showSnackBar;
import static com.guzzler.go4lunch_p7.utils.ShowToastSnack.showToast;

public class LoginActivity extends Activity {

    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private final int RC_SIGN_IN = 123;

    boolean doubleBackToExitPressedOnce = false;

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


        // 3 - Launch Sign-In Activity
        this.startSignInActivity();
    }

    // --------------------
    // NAVIGATION
    // --------------------

    // 2 - Launch Sign-In Activity
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo_loggin)
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(), // SUPPORT GOOGLE
                                new AuthUI.IdpConfig.GitHubBuilder().build(), // SUPPORT GITHUB
                                new AuthUI.IdpConfig.TwitterBuilder().build(), // SUPPORT TWITTER
                                new AuthUI.IdpConfig.EmailBuilder().build(), // SUPPORT EMAIL
                                new AuthUI.IdpConfig.FacebookBuilder().build())) // SUPPORT FACEBOOK
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        showToast(this, "Please click BACK again to return to login", 0);

        new Handler(Looper.getMainLooper()).postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle SignIn Activity response on activity result
        handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                createWorkmate();
                finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else { // ERRORS

                // Show Snack Bar with a message
                if (response == null) {
                    showSnackBar(binding.logActivityCoordinatorLayout, getString(R.string.error_authentication_canceled), 0);
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(binding.logActivityCoordinatorLayout, getString(R.string.error_no_internet), 0);
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(binding.logActivityCoordinatorLayout, getString(R.string.error_unknown_error), 0);
                }
            }
        }
    }

    protected OnFailureListener onFailureListener() {
        return e -> showToast(getApplicationContext(), getString(R.string.error_unknown_error), 1);
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }


    // Méthode utilisée pour créer l'utilisateur qui s'est connecté avec succès et sutout qui n'existe pas déjà
    private void createWorkmate() {
        if (getCurrentUser() != null) {
            UserHelper.getWorkmate(getCurrentUser().getUid()).addOnCompleteListener(UserTask -> {
                        if (UserTask.isSuccessful()) {
                            if (!UserTask.getResult().exists()) {
                                String urlPicture = (getCurrentUser().getPhotoUrl() != null) ? getCurrentUser().getPhotoUrl().toString() : null;
                                if (getCurrentUser().getDisplayName() != null) {
                                    String name = getCurrentUser().getDisplayName();
                                    String uid = getCurrentUser().getUid();
                                    UserHelper.createWorkmate(uid, urlPicture, name).addOnFailureListener(onFailureListener());
                                } else {
                                    String name = getCurrentUser().getEmail();
                                    String uid = getCurrentUser().getUid();
                                    UserHelper.createWorkmate(uid, urlPicture, name).addOnFailureListener(onFailureListener());
                                }
                            }
                        }
                    }
            );
        }
    }
}