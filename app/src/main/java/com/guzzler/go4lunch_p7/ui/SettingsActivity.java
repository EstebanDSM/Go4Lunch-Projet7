package com.guzzler.go4lunch_p7.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.guzzler.go4lunch_p7.R;
import com.guzzler.go4lunch_p7.api.firebase.UserHelper;
import com.guzzler.go4lunch_p7.utils.notifications.NotificationHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity {
    protected SharedViewModel mSharedViewModel;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.activity_main_toolbar)
    Toolbar mToolbar;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.settings_switch)
    SwitchCompat mSwitch;

    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        configureToolbar();
        retrieveUserSettings();
        setListenerAndFilters();
        createNotificationHelper();
        setTitle(getString(R.string.settings_toolbar));
    }


    private void configureToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void retrieveUserSettings() {
        UserHelper.getWorkmatesCollection().document(getCurrentUser().getUid()).addSnapshotListener((documentSnapshot, e) -> {
            if (e != null) {
                Log.e("TAG", "Listen failed.", e);
                return;
            }
            if (documentSnapshot != null && documentSnapshot.exists()) {
                Log.e("TAG", "Current data: " + documentSnapshot.getData());
                mSwitch.setChecked(documentSnapshot.getData().get("notification").equals(true));
                if (documentSnapshot.getData().get("notification").equals(true)) {
                    mNotificationHelper.scheduleRepeatingNotification();
                }
            } else {
                Log.e("TAG", "Current data: null");
            }
        });
    }

    private void setListenerAndFilters() {
        mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed() && buttonView.isChecked()) {
                UserHelper.updateUserSettings(getCurrentUser().getUid(), true);
                Toast.makeText(getApplication(), "NOTIFICATIONS ON", Toast.LENGTH_SHORT).show();
                mNotificationHelper.scheduleRepeatingNotification();


            } else if (!buttonView.isChecked()) {
                UserHelper.updateUserSettings(getCurrentUser().getUid(), false);
                Toast.makeText(getApplication(), "NOTIFICATIONS OFF", Toast.LENGTH_SHORT).show();
                mNotificationHelper.cancelAlarmRTC();
            }
        });
    }

    private void createNotificationHelper() {
        mNotificationHelper = new NotificationHelper(getBaseContext());

    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}